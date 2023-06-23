package com.brieuclf.library.server.behaviorspec

import com.brieuclf.library.infra.entity.AuthorEntity
import com.brieuclf.library.infra.entity.BookEntity
import com.brieuclf.library.server.tooling.IntegrationTest
import com.brieuclf.library.server.tooling.PostgresManager
import io.kotest.assertions.json.FieldComparison
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.net.URI
import java.util.UUID
import org.springframework.boot.test.web.server.LocalServerPort

@IntegrationTest
class BookCommandsTest(@LocalServerPort serverPort: String) : BehaviorSpec({

    beforeEach {
        PostgresManager.givenBooksTableIsEmpty()
        PostgresManager.givenAuthorsTableIsEmpty()
    }

    val client = HttpClient(CIO)

    val bookId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    Given("An author") {
        PostgresManager.givenAuthorTableHasAuthor(
            AuthorEntity(
                id = bookId, firstName = "J. R. R.", lastName = "Tolkien"
            )
        )
        And("A book") {
            When("I create the book") {
                val response = client.post(URI("http://localhost:${serverPort}/api/books").toURL()) {
                    contentType(ContentType.Application.Json)
                    setBody(
                        """
                    {
                        "title": "The Lord of the Rings",
                        "authorId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
                    }
                    """.trimIndent()
                    )
                }
                Then("I get a 201") {
                    response.status shouldBe HttpStatusCode.Created
                }
                And("I get the book information") {
                    response.body<String>() shouldEqualJson {
                        fieldComparison = FieldComparison.Lenient
                        """
                    {
                        "title": "The Lord of the Rings",
                        "authorId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
                    }
                """.trimIndent()
                    }
                }
            }
        }
    }

    Given("A book") {
        PostgresManager.givenBookTableHasBook(
            BookEntity(
                id = bookId,
                title = "The Lord of the Rings",
                authorId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"),
                isBorrowed = false
            )
        )
        When("I borrow the book") {
            val response = client.post(URI("http://localhost:${serverPort}/api/books/${bookId}/borrow").toURL())
            Then("I get a 202") {
                response.status shouldBe HttpStatusCode.Accepted
            }
            And("I get the book information") {
                response.body<String>().shouldBeEmpty()
            }
        }
    }

    Given("A book with no title") {
        When("I create the book") {
            val response = client.post(URI("http://localhost:${serverPort}/api/books").toURL()) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {
                        "authorId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
                    }
                    """.trimIndent()
                )
            }
            Then("I get a 400") {
                response.status shouldBe HttpStatusCode.BadRequest
            }
            And("I get the error information") {
                response.body<String>() shouldEqualJson {
                    fieldComparison = FieldComparison.Lenient
                    """
                    {
                        "path": "/api/books",
                        "status": 400,
                        "error": "Bad Request"
                    }
                """.trimIndent()
                }
            }
        }
    }

})