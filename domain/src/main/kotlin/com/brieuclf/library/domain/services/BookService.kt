package com.brieuclf.library.domain.services

import com.brieuclf.library.domain.model.BorrowBookInformation
import com.brieuclf.library.domain.model.Borrowed
import com.brieuclf.library.domain.model.CreateBookInformation
import com.brieuclf.library.domain.ports.AuthorRepository
import com.brieuclf.library.domain.ports.BookRepository
import com.brieuclf.library.domain.ports.BorrowingRepository
import java.util.UUID
import kotlinx.datetime.Clock.System.now

class BookService(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val borrowingRepository: BorrowingRepository
) {

    suspend fun createBook(createBookInformation: CreateBookInformation) =
        authorRepository.findById(createBookInformation.authorId)?.let {
            bookRepository.create(createBookInformation)
        }

    suspend fun borrowBook(bookId: UUID) =
        bookRepository.findById(bookId)?.let { book ->
            if (!book.borrowing.isBorrowed) {
                borrowingRepository.create(BorrowBookInformation(bookId = bookId, instant = now()))
                bookRepository.update(
                    book.copy(
                        borrowing = Borrowed(
                            isBorrowed = true,
                            borrowedInstant = now(),
                        )
                    )
                )
            } else {
                throw Exception("book with id ${book.id} is not available for borrowing")
            }
        }

    suspend fun returnBook(bookId: UUID) = bookRepository.findById(bookId)?.let {
        if (it.borrowing.isBorrowed) {
            bookRepository.update(
                it.copy(
                    borrowing = Borrowed(
                        isBorrowed = false,
                        borrowedInstant = null,
                    )
                )
            )
        } else {
            throw Exception("book with id $bookId is not borrowed")
        }
    }

    suspend fun getBookById(id: UUID) = bookRepository.findById(id)

    suspend fun isBookBorrowed(id: UUID) =
        bookRepository.findById(id)?.borrowing?.isBorrowed ?: throw Exception("Book with id $id not found")

    suspend fun getBooks() = bookRepository.findAll()
}
