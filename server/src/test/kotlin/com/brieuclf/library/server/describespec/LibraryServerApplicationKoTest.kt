package com.brieuclf.library.server.describespec

import com.brieuclf.library.domain.model.Author
import com.brieuclf.library.infra.entity.AuthorEntity
import com.brieuclf.library.infra.entity.BookEntity
import com.brieuclf.library.server.mappers.toDomain
import com.brieuclf.library.server.tooling.IntegrationTest
import com.brieuclf.library.server.tooling.PostgresManager
import com.brieuclf.library.server.tooling.PostgresManager.Companion.givenAuthorTableHasAuthor
import com.brieuclf.library.server.tooling.PostgresManager.Companion.givenAuthorTableHasAuthors
import io.kotest.assertions.json.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.restassured.http.ContentType
import io.restassured.http.ContentType.JSON
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import java.util.UUID
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus

@IntegrationTest
class LibraryServerApplicationKoTest : DescribeSpec({

    beforeEach {
        PostgresManager.givenBooksTableIsEmpty()
        PostgresManager.givenAuthorsTableIsEmpty()
    }

    describe("Commands") {
        it("Can create an author with firstName and lastName") {
            Given {
                body(
                    """
                  {"firstName": "John", "lastName": "Doe"}
                """.trimIndent()
                )
                contentType(JSON)
            } When {
                post("/api/authors")
            } Then {
                statusCode(201)
                val body = extract().body().asString()
                body shouldEqualJson {
                    fieldComparison = FieldComparison.Lenient
                    """
                        {
                            "firstName": "John",
                            "lastName": "Doe"
                        }
                    """.trimIndent()
                }

            }

        }

        it("Updates borrowedInstant and isBorrowed fields when I borrow a book") {
            val bookId = UUID.fromString("b0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0")
            val authorId = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0")
            givenAuthorTableHasAuthor(AuthorEntity("John R.R.", "Tolkien", authorId))
            PostgresManager.givenBookTableHasBook(
                BookEntity(
                    bookId, "The Lord of the Rings", authorId, false, ""
                )
            )

            Given {
                contentType(JSON)
            } When {
                post("/api/books/${bookId}/borrow")
            } Then {
                statusCode(HttpStatus.ACCEPTED.value())
            }

            val updatedBook = PostgresManager.getBookFromTable(bookId)!!
            updatedBook.isBorrowed shouldBe true
            Assertions.assertTrue(Instant.parse(updatedBook.borrowedInstant) < now())
        }

        it("Adds a book entry to datatable when I create a book") {

            givenAuthorTableHasAuthor(
                AuthorEntity(
                    id = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0"),
                    firstName = "John R.R.",
                    lastName = "Tolkien"
                )
            )
            Given {
                body(
                    """
                  {"title": "The Lord of the Rings", "authorId": "a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0"}
                """.trimIndent()
                )
                contentType(JSON)
            } When {
                post("/api/books")
            } Then {
                statusCode(201)
                val body = extract().body().asString()
                body shouldEqualJson {
                    fieldComparison = FieldComparison.Lenient
                    """
                        {
                            "title": "The Lord of the Rings",
                            "authorId": "a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0",
                            "borrowing": {
                                "isBorrowed": false,
                                "borrowedInstant": null
                            }
                        }
                    """.trimIndent()
                }
            }
        }
    }

    describe("Queries") {
        it("Returns all books when I query all books") {
            val authorId = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0")
            givenAuthorTableHasAuthor(
                AuthorEntity(
                    id = authorId, firstName = "John R.R.", lastName = "Tolkien"
                )
            )
            PostgresManager.givenBookTableHasBook(
                BookEntity(
                    id = UUID.fromString("b0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0"),
                    title = "The Lord of the Rings",
                    authorId = authorId,
                    isBorrowed = false,
                    borrowedInstant = ""
                )
            )
            PostgresManager.givenBookTableHasBook(
                BookEntity(
                    id = UUID.fromString("b0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e1"),
                    title = "The Hobbit",
                    authorId = authorId,
                    isBorrowed = false,
                    borrowedInstant = ""
                )
            )

            Given {
                contentType(JSON)
            } When {
                get("/api/books")
            } Then {
                statusCode(200)
                extract().body().asString() shouldEqualJson {
                    fieldComparison = FieldComparison.Lenient
                    """
                        [
                            {
                                "title": "The Lord of the Rings",
                                "authorId": "a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0",
                                "borrowing": {
                                    "isBorrowed": false,
                                    "borrowedInstant": null
                                }
                            },
                            {
                                "title": "The Hobbit",
                                "authorId": "a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0",
                                "borrowing": {
                                    "isBorrowed": false,
                                    "borrowedInstant": null
                                }
                            }
                        ]
                    """.trimIndent()
                }
            }
        }

        it("Returns all authors when I query all authors") {
            givenAuthorTableHasAuthor(
                AuthorEntity(
                    id = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0"),
                    firstName = "John R.R.",
                    lastName = "Tolkien"
                )
            )
            givenAuthorTableHasAuthor(
                AuthorEntity(
                    id = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e1"),
                    firstName = "Pierre",
                    lastName = "Bottero"
                )
            )
            var responseBodyAuthors: Array<Author>

            Given {
                contentType(JSON)
            } When {
                get("/api/authors")
            } Then {
                statusCode(200)

                responseBodyAuthors = extract().`as`(Array<Author>::class.java)
                responseBodyAuthors shouldContainAll listOf(
                    Author(
                        id = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e0"),
                        firstName = "John R.R.",
                        lastName = "Tolkien"
                    ), Author(
                        id = UUID.fromString("a0e0a0e0-a0e0-a0e0-a0e0-a0e0a0e0a0e1"),
                        firstName = "Pierre",
                        lastName = "Bottero"
                    )
                )
            }
        }

        it("Lists a great quantity of authors") {
            val authorsToCheck = mutableListOf<Author>()
            val authors = (1..150).map {
                AuthorEntity(
                    id = UUID.randomUUID(), firstName = "John R.R.", lastName = "Tolkien"
                ).also { authorsToCheck.add(it.toDomain()) }
            }
            givenAuthorTableHasAuthors(authors)

            lateinit var responseBodyAuthors: Array<Author>
            Given {
                contentType(JSON)
            } When {
                get("/api/authors")
            } Then {
                statusCode(200)
                responseBodyAuthors = extract().`as`(Array<Author>::class.java)
            }
            responseBodyAuthors.size shouldBe 150
            responseBodyAuthors shouldContainAll authorsToCheck
        }
    }
})
