package com.brieuclf.library.server.tooling

import com.brieuclf.library.infra.entity.AuthorEntity
import com.brieuclf.library.infra.entity.BookEntity
import io.r2dbc.spi.Row
import java.util.UUID
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.flywaydb.core.Flyway
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener
import org.testcontainers.containers.PostgreSQLContainer


class PostgresManager : TestExecutionListener {

    private val container = PostgreSQLContainer("postgres:14.1-alpine")

    init {
        container.start();
        with(System::setProperty) {
            invoke("spring.r2dbc.url", container.jdbcUrl.replace("jdbc", "r2dbc"))
            invoke("spring.r2dbc.username", container.username)
            invoke("spring.r2dbc.password", container.password)
            invoke("spring.flyway.url", container.jdbcUrl)
            invoke("spring.flyway.user", container.username)
            invoke("spring.flyway.password", container.password)
        }
    }

    override fun beforeTestMethod(testContext: TestContext) {
        testContext.applicationContext.getBean(Flyway::class.java).migrate()
    }

    override fun beforeTestClass(testContext: TestContext) {
        databaseClient = testContext.applicationContext.getBean(DatabaseClient::class.java)
    }

    companion object {
        private lateinit var databaseClient: DatabaseClient

        suspend fun givenBooksTableIsEmpty() =
            databaseClient.sql("truncate table books").fetch().awaitRowsUpdated()

        suspend fun givenAuthorsTableIsEmpty() =
            databaseClient.sql("truncate table authors").fetch().awaitRowsUpdated()

        suspend fun givenBookTableHasBook(book: BookEntity): Long {
            val bookQueryWithBorrowedInstant = """
                    insert into books (id, title, author_id, is_borrowed, borrowed_instant) values (:id, :title, :authorId, :isBorrowed, :borrowedInstant) 
                    on conflict (id) do update set title = :title, author_id = :authorId, is_borrowed = :isBorrowed, borrowed_instant = :borrowedInstant
                    """.trimIndent()
            val bookQueryWithoutBorrowedInstant = """
                    insert into books (id, title, author_id, is_borrowed) values (:id, :title, :authorId, :isBorrowed) 
                    on conflict (id) do update set title = :title, author_id = :authorId, is_borrowed = :isBorrowed
                    """.trimIndent()
            return if (book.borrowedInstant != "") {
                databaseClient.sql(
                    bookQueryWithBorrowedInstant
                )
                    .bind("id", book.id!!)
                    .bind("title", book.title)
                    .bind("authorId", book.authorId)
                    .bind("isBorrowed", book.isBorrowed)
                    .bind("borrowedInstant", book.borrowedInstant)
            } else {
                databaseClient.sql(
                    bookQueryWithoutBorrowedInstant
                )
                    .bind("id", book.id!!)
                    .bind("title", book.title)
                    .bind("authorId", book.authorId)
                    .bind("isBorrowed", book.isBorrowed)
            }.fetch()
                .awaitRowsUpdated()
        }

        suspend fun givenAuthorTableHasAuthor(author: AuthorEntity) = databaseClient.sql(
            """
            insert into authors (id, first_name, last_name) values (:id, :firstName, :lastName) 
            on conflict (id) do update set first_name = :firstName, last_name = :lastName
            """.trimIndent()
        )
            .bind("id", author.id!!)
            .bind("firstName", author.firstName)
            .bind("lastName", author.lastName)
            .fetch()
            .awaitRowsUpdated()

        suspend fun givenAuthorTableHasAuthors(authors: List<AuthorEntity>) = authors.map { givenAuthorTableHasAuthor(it) }

        suspend fun getBookFromTable(bookId: UUID) = databaseClient.sql(
            """
                select id, title, author_id, is_borrowed, borrowed_instant from books where id = :id
            """.trimIndent()
        ).bind("id", bookId)
            .map { row, _ ->
                BookEntity(
                    id = row.getCol("id"),
                    title = row.getCol("title")!!,
                    authorId = row.getCol("author_id")!!,
                    isBorrowed = row.getCol("is_borrowed")!!,
                    borrowedInstant = row.getCol("borrowed_instant")!!,
                )
            }
            .one()
            .awaitSingleOrNull()

        private inline fun <reified T> Row.getCol(columnName: String) = get(columnName, T::class.java)
    }
}
