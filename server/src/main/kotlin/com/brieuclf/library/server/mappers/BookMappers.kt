package com.brieuclf.library.server.mappers

import com.brieuclf.library.domain.model.Book
import com.brieuclf.library.domain.model.Borrowed
import com.brieuclf.library.domain.model.CreateBookInformation
import com.brieuclf.library.infra.entity.BookEntity
import kotlinx.datetime.Instant

fun BookEntity.toDomain() = Book(
    title = title,
    authorId = authorId,
    id = id!!,
    borrowing = Borrowed(
        isBorrowed = isBorrowed,
        borrowedInstant = if (borrowedInstant != "") Instant.parse(borrowedInstant) else null
    )
)

fun Book.toEntity() = BookEntity(id, title, authorId, borrowing.isBorrowed, borrowing.borrowedInstant.toString())

fun CreateBookInformation.toEntity() = BookEntity(
    title = title,
    authorId = authorId,
    isBorrowed = false
)
