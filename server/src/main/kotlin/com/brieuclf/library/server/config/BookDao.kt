package com.brieuclf.library.server.config

import com.brieuclf.library.domain.model.Book
import com.brieuclf.library.domain.model.CreateBookInformation
import com.brieuclf.library.domain.ports.BookRepository
import com.brieuclf.library.infra.entity.BookEntity
import com.brieuclf.library.infra.repository.BookCrudRepository
import com.brieuclf.library.server.mappers.toDomain
import com.brieuclf.library.server.mappers.toEntity
import java.util.UUID
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class BookDao(private val bookRepository: BookCrudRepository) : BookRepository {
    override suspend fun findById(id: UUID) = bookRepository.findById(id)?.toDomain()
    override suspend fun create(createBookInformation: CreateBookInformation) =
        bookRepository.save(createBookInformation.toEntity()).toDomain()

    override suspend fun update(book: Book): Book = bookRepository.save(book.toEntity()).toDomain()

    override suspend fun findAll() = bookRepository.findAll().map(BookEntity::toDomain)
}