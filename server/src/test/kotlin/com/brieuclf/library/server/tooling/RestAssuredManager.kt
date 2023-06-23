package com.brieuclf.library.server.tooling

import io.restassured.RestAssured
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener


class RestAssuredManager : TestExecutionListener {

    override fun beforeTestMethod(testContext: TestContext) {
        RestAssured.port = testContext.applicationContext.environment
            .getRequiredProperty("local.server.port", Int::class.java)
        RestAssured.filters(RequestLoggingFilter(), ResponseLoggingFilter())
    }
}
