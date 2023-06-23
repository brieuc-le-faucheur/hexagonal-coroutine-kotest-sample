package com.brieuclf.library.server.handlers.command

import com.brieuclf.library.domain.LibraryApis
import com.brieuclf.library.domain.model.*
import com.brieuclf.library.server.commands.CreateAuthorCommand
import com.brieuclf.library.server.commands.CreateBookCommand
import java.util.UUID
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("api")
class LibraryCommandHandler(private val libraryApis: LibraryApis) {

    @PostMapping("books")
    @ResponseStatus(CREATED)
    suspend fun createBook(@RequestBody command: CreateBookCommand) =
        libraryApis.bookService.createBook(CreateBookInformation(command.title, command.authorId))

    @PostMapping("authors")
    @ResponseStatus(CREATED)
    suspend fun createAuthor(@RequestBody command: CreateAuthorCommand) =
        libraryApis.authorService.createAuthor(CreateAuthorInformation(
            firstName = command.firstName,
            lastName = command.lastName
        ))

    @PostMapping("books/{bookId}/borrow")
    @ResponseStatus(ACCEPTED)
    suspend fun borrowBook(@PathVariable("bookId") bookId: UUID) {
        libraryApis.bookService.borrowBook(bookId)
    }

    @GetMapping("books/{bookId}/return")
    @ResponseStatus(ACCEPTED)
    suspend fun returnBook(@PathVariable("bookId") bookId: UUID) {
        libraryApis.bookService.returnBook(bookId)
    }
}




