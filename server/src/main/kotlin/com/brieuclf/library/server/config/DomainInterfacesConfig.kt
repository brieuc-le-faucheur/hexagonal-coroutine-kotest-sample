package com.brieuclf.library.server.config

import com.brieuclf.library.domain.LibraryApis
import com.brieuclf.library.domain.services.AuthorService
import com.brieuclf.library.domain.services.BookService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebFilter


@Configuration
class DomainInterfacesConfig(
    private val bookDao: BookDao,
    private val authorDao: AuthorDao,
    private val borrowingDao: BorrowingDao
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun libraryApis() = LibraryApis(AuthorService(authorDao), BookService(bookDao, authorDao, borrowingDao))

    @Bean
    fun loggingFilter(): WebFilter =
        WebFilter { exchange, chain ->
            val request = exchange.request
            logger.info("Processing request method=${request.method} path=${request.path.pathWithinApplication()} params=[${request.queryParams}] body=[${request.body}]")

            val result = chain.filter(exchange)

            logger.info("Handling with response ${exchange.response}")

            return@WebFilter result
        }

}
