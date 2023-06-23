package com.brieuclf.library.domain.services

import com.brieuclf.library.domain.model.Author
import com.brieuclf.library.domain.model.Book
import com.brieuclf.library.domain.model.Borrowed
import com.brieuclf.library.domain.model.CreateBookInformation
import com.brieuclf.library.domain.ports.AuthorRepository
import com.brieuclf.library.domain.ports.BookRepository
import com.brieuclf.library.domain.ports.BorrowingRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.UUID.randomUUID

class BookServiceFunSpecTest : FunSpec({

    val bookRepository = mockk<BookRepository>()
    val authorRepository = mockk<AuthorRepository>()
    val borrowingRepository = mockk<BorrowingRepository>()

    val bookService = BookService(bookRepository, authorRepository, borrowingRepository);

    val authorId = randomUUID()
    val bookId = randomUUID()

    context("BookService.createBook") {
        test("Creates a book when author exists") {

            // Given
            coEvery { authorRepository.findById(authorId) } returns Author("Bottero", "Pierre", authorId)
            coEvery { bookRepository.create(any()) } returns Book("Ewilan", authorId = authorId, id = bookId)

            // When
            val createBookPojo = CreateBookInformation("Ewilan", authorId)
            val bookCreated = bookService.createBook(createBookPojo)

            // Then
            coVerify(exactly = 1) { authorRepository.findById(authorId) }
            coVerify(exactly = 1) { bookRepository.create(eq(createBookPojo)) }
            bookCreated shouldBe Book("Ewilan", authorId = authorId, id = bookId)
        }

        test("Does not create book when author does not exist") {

            // Given
            coEvery { authorRepository.findById(authorId) } returns null

            // When
            bookService.createBook(CreateBookInformation("Ewilan", authorId))

            // Then
            coVerify(exactly = 1) { authorRepository.findById(authorId) }
            coVerify(exactly = 0) { bookRepository.create(any()) }
        }
    }

    context("BookService.isBookBorrowed") {
        test("Returns true when book is borrowed") {

            coEvery { bookRepository.findById(bookId) } returns Book(
                "Ewilan",
                authorId = authorId,
                id = bookId,
                borrowing = Borrowed(true)
            )

            bookService.isBookBorrowed(bookId) shouldBe true
        }

        test("Returns false when book is not borrowed") {

            coEvery { bookRepository.findById(bookId) } returns Book(
                "Ewilan",
                authorId = authorId,
                id = bookId,
                borrowing = Borrowed(false)
            )

            bookService.isBookBorrowed(bookId) shouldBe false
        }
    }
})
