package com.brieuclf.library.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@SpringBootApplication(scanBasePackages = ["com.brieuclf.library.server", "com.brieuclf.library.infra"])
@EnableR2dbcRepositories(basePackages = ["com.brieuclf.library.server", "com.brieuclf.library.infra"])
class LibraryApplication

fun main(args: Array<String>) {
    runApplication<LibraryApplication>(*args)
}

