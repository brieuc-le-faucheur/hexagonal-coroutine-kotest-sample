package com.brieuclf.library.server.tooling

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.TestExecutionListeners

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Retention(RUNTIME)
@Target(CLASS)
@TestExecutionListeners(listeners = [PostgresManager::class, RestAssuredManager::class])
annotation class IntegrationTest
