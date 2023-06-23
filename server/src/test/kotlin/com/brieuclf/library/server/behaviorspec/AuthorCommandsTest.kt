package com.brieuclf.library.server.behaviorspec

import com.brieuclf.library.server.tooling.IntegrationTest
import com.brieuclf.library.server.tooling.PostgresManager
import io.kotest.assertions.json.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import java.net.URI
import org.springframework.boot.test.web.server.LocalServerPort

@IntegrationTest
class AuthorCommandsTest(@LocalServerPort serverPort: String) : BehaviorSpec({

    beforeEach {
        PostgresManager.givenBooksTableIsEmpty()
        PostgresManager.givenAuthorsTableIsEmpty()
    }

    val client = HttpClient(CIO)

    Given("A new author") {
        When("I create the author") {
            val response = client.post(URI("http://localhost:${serverPort}/api/authors").toURL()) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {
                        "firstName": "John",
                        "lastName": "Doe"
                    }
                    """.trimIndent()
                )
            }
            Then("I get a 201") {
                response.status shouldBe HttpStatusCode.Created
            }
            And("I get the author information") {
                response.body<String>() shouldEqualJson {
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
    }

    Given("An author with no first name") {
        When("I try to create the author") {
            val response = client.post(URI("http://localhost:${serverPort}/api/authors").toURL()) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {
                        "lastName": "Doe"
                    }
                    """.trimIndent()
                )
            }
            Then("I get a 400") {
                response.status shouldBe HttpStatusCode.BadRequest
            }
            And("I get an error message") {
                response.body<String>() shouldEqualJson {
                    fieldComparison = FieldComparison.Lenient
                    """
                    {
                        "path": "/api/authors",
                        "status": 400,
                        "error": "Bad Request"
                    }
                """.trimIndent()
                }
            }
        }
    }

    Given("An author with no last name") {
        When("I try to create the author") {
            val response = client.post(URI("http://localhost:${serverPort}/api/authors").toURL()) {
                contentType(ContentType.Application.Json)
                setBody(
                    """
                    {
                        "firstName": "John"
                    }
                    """.trimIndent()
                )
            }
            Then("I get a 400") {
                response.status shouldBe HttpStatusCode.BadRequest
            }
            And("I get an error message") {
                response.body<String>() shouldEqualJson {
                    fieldComparison = FieldComparison.Lenient
                    """
                    {
                        "path": "/api/authors",
                        "status": 400,
                        "error": "Bad Request"
                    }
                """.trimIndent()
                }
            }
        }
    }
})
