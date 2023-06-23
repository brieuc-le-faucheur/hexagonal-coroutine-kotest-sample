package com.brieuclf.library.domain.services

import com.brieuclf.library.domain.model.Author
import com.brieuclf.library.domain.model.Book
import com.brieuclf.library.domain.model.Borrowed
import com.brieuclf.library.domain.model.CreateBookInformation
import com.brieuclf.library.domain.ports.AuthorRepository
import com.brieuclf.library.domain.ports.BookRepository
import com.brieuclf.library.domain.ports.BorrowingRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.UUID

class BookServiceBehaviorSpecTest : BehaviorSpec({
    val bookRepository = mockk<BookRepository>()
    val authorRepository = mockk<AuthorRepository>()
    val borrowingRepository = mockk<BorrowingRepository>()

    val bookService = BookService(bookRepository, authorRepository, borrowingRepository)

    val authorId = UUID.randomUUID()
    val bookId = UUID.randomUUID()

    Given("Required author exists") {
        coEvery { authorRepository.findById(authorId) } returns Author("Bottero", "Pierre", authorId)
        And("bookRepository.create returns a book") {
            coEvery { bookRepository.create(any()) } returns Book("Ewilan", authorId = authorId, id = bookId)

            When("we call createBook") {
                val createBookPojo = CreateBookInformation("Ewilan", authorId)
                val bookCreated = bookService.createBook(createBookPojo)

                Then("authorRepository is called with the book author") {
                    coVerify(exactly = 1) { authorRepository.findById(authorId) }
                }
                And("bookRepository is called once with author and book information") {
                    coVerify(exactly = 1) { bookRepository.create(eq(createBookPojo)) }
                }
                And("created book information matches the expected one") {
                    bookCreated shouldBe Book("Ewilan", authorId = authorId, id = bookId)
                }
            }
        }
    }

    Given("Required author does not exist") {
        coEvery { authorRepository.findById(authorId) } returns null
        When("we call createBook") {
            val createBookPojo = CreateBookInformation("Ewilan", authorId)
            bookService.createBook(createBookPojo)
            Then("authorRepository is called with the book author") {
                coVerify(exactly = 1) { authorRepository.findById(authorId) }
            }
            And("bookRepository is called once with author and book information") {
                coVerify(exactly = 0) { bookRepository.create(any()) }
            }
        }
    }
    Given("BookService returns a book with borrowed information") {

        When("book is borrowed") {
            coEvery { bookRepository.findById(bookId) } returns Book(
                "Ewilan", authorId = authorId, id = bookId, borrowing = Borrowed(true)
            )
            Then("bookService.isBookBorrowed returns true") {
                bookService.isBookBorrowed(bookId) shouldBe true
            }
        }
        When("book is not borrowed") {
            coEvery { bookRepository.findById(bookId) } returns Book(
                "Ewilan", authorId = authorId, id = bookId, borrowing = Borrowed(false)
            )
            Then("bookService.isBookBorrowed returns false") {
                bookService.isBookBorrowed(bookId) shouldBe false
            }
        }
    }
})