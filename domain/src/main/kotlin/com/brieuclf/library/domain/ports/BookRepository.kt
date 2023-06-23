package com.brieuclf.library.domain.ports

import com.brieuclf.library.domain.model.Book
import com.brieuclf.library.domain.model.CreateBookInformation
import java.util.UUID
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun findById(id: UUID): Book?

    suspend fun create(createBookInformation: CreateBookInformation): Book
    suspend fun update(book: Book): Book
    suspend fun findAll(): Flow<Book>
}