package com.brieuclf.library.server.handlers.query

import com.brieuclf.library.domain.LibraryApis
import java.util.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api")
class LibraryQueryHandler(
private val libraryDomainApis: LibraryApis) {

    @GetMapping("books")
    suspend fun getBooks() = libraryDomainApis.bookService.getBooks()

    @GetMapping("books/{id}")
    suspend fun getBook(@PathVariable id: UUID) = libraryDomainApis.bookService.getBookById(id)

    @GetMapping("books/{id}/borrowed")
    suspend fun isBookBorrowed(@PathVariable id: UUID) = libraryDomainApis.bookService.isBookBorrowed(id)

    @GetMapping("authors")
    suspend fun getAuthors() = libraryDomainApis.authorService.getAuthors()

    @GetMapping("authors/{id}")
    suspend fun getAuthor(@PathVariable id: UUID) = libraryDomainApis.authorService.getAuthor(id)
}