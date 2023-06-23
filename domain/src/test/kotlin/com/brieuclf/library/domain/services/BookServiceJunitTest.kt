package com.brieuclf.library.domain.services

import com.brieuclf.library.domain.model.Author
import com.brieuclf.library.domain.model.Book
import com.brieuclf.library.domain.model.Borrowed
import com.brieuclf.library.domain.model.CreateBookInformation
import com.brieuclf.library.domain.ports.AuthorRepository
import com.brieuclf.library.domain.ports.BookRepository
import com.brieuclf.library.domain.ports.BorrowingRepository
import java.util.UUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock.System.now
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class BookServiceJunitTest {

    private val bookRepository: BookRepository = mock()

    private val authorRepository: AuthorRepository = mock()

    private val borrowingRepository: BorrowingRepository = mock()

    private lateinit var bookService: BookService

    private val authorId: UUID = UUID.randomUUID()
    private val bookId: UUID = UUID.randomUUID()

    @Test
    fun bookService_createBook_creates_a_book_when_author_exists() = runTest {
        `when`(authorRepository.findById(authorId)).thenReturn(Author("Herbert", "Frank", authorId))
        `when`(
            bookRepository.create(
                CreateBookInformation(
                    "Dune",
                    authorId
                )
            )
        ).thenReturn(Book("Dune", authorId = authorId, id = bookId))

        bookService = BookService(bookRepository, authorRepository, borrowingRepository);

        val createBookPojo = CreateBookInformation("Dune", authorId)
        val bookCreated = bookService.createBook(createBookPojo)

        verify(authorRepository, times(1)).findById(authorId)
        verify(bookRepository, times(1)).create(createBookPojo)
        Assertions.assertEquals(Book("Dune", authorId = authorId, id = bookId), bookCreated)
    }

    @Test
    fun bookService_isBookBorrowed_returns_true_when_book_is_borrowed() = runTest {
        `when`(bookRepository.findById(bookId)).thenReturn(
            Book(
                "Dune",
                authorId,
                bookId,
                Borrowed(true, now())
            )
        )
        bookService = BookService(bookRepository, authorRepository, borrowingRepository);
        assertTrue(bookService.isBookBorrowed(bookId))
    }

    @Test
    fun bookService_isBookBorrowed_returns_false_when_book_is_not_borrowed() = runTest {
        `when`(bookRepository.findById(bookId)).thenReturn(
            Book(
                "Dune",
                authorId,
                bookId,
                Borrowed(false, null)
            )
        )
        bookService = BookService(bookRepository, authorRepository, borrowingRepository);
        assertFalse(bookService.isBookBorrowed(bookId))
    }
}