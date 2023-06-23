package com.brieuclf.library.server.jupiter

import com.brieuclf.library.infra.entity.AuthorEntity
import com.brieuclf.library.infra.entity.BookEntity
import com.brieuclf.library.server.tooling.IntegrationTest
import com.brieuclf.library.server.tooling.PostgresManager.Companion.getBookFromTable
import com.brieuclf.library.server.tooling.PostgresManager.Companion.givenAuthorTableHasAuthor
import com.brieuclf.library.server.tooling.PostgresManager.Companion.givenAuthorsTableIsEmpty
import com.brieuclf.library.server.tooling.PostgresManager.Companion.givenBookTableHasBook
import com.brieuclf.library.server.tooling.PostgresManager.Companion.givenBooksTableIsEmpty
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import java.util.UUID
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.ACCEPTED

@IntegrationTest
class LibraryServerApplicationTests {

    @BeforeEach
    fun emptyDataTables() {
        runBlocking {
            givenBooksTableIsEmpty()
            givenAuthorsTableIsEmpty()
        }
    }

    @Test
    fun app_creates_author() {
        given()
            .body(
                """
                    {
                        "firstName": "Frank",
                        "lastName": "Herbert"
                    }
                """.trimIndent()
            )
            .contentType(JSON)
            .`when`()
            .post("/api/authors")
            .then()
            .statusCode(201)
            .body("firstName", `is`("Frank"))
            .body("lastName", `is`("Herbert"))
            .body("id", notNullValue())
    }

    @Test
    fun borrow_book() = runTest {
        val bookId = UUID.fromString("b0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0")
        val authorId = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0")
        givenAuthorTableHasAuthor(AuthorEntity("Frank", "Herbert", authorId))
        givenBookTableHasBook(
            BookEntity(
                bookId, "Dune", authorId, false, ""
            )
        )

        given()
            .contentType(JSON)
            .`when`().post("/api/books/${bookId}/borrow").then()
            .statusCode(ACCEPTED.value())

        val updatedBook = getBookFromTable(bookId)!!
        assertTrue(updatedBook.isBorrowed)
        assertTrue(Instant.parse(updatedBook.borrowedInstant) < now())
    }

    @Test
    fun app_creates_book() = runTest {

        givenAuthorTableHasAuthor(
            AuthorEntity(
                id = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0"), firstName = "Frank", lastName = "Herbert"
            )
        )
        given().body(
            """
                  {"title": "Dune", "authorId": "a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0"}
                """.trimIndent()
        )
            .contentType(JSON)
            .post("/api/books").then().statusCode(201)
    }
}
