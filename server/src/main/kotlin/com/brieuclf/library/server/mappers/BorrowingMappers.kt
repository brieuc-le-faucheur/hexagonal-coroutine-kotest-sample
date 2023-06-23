package com.brieuclf.library.server.mappers

import com.brieuclf.library.domain.model.BorrowBookInformation
import com.brieuclf.library.domain.model.Borrowing
import com.brieuclf.library.infra.entity.BorrowingEntity
import kotlinx.datetime.toInstant

fun BorrowBookInformation.toEntity() = BorrowingEntity(
    bookId = bookId,
    instant = instant.toString()
)

fun BorrowingEntity.toDomain() = Borrowing(
    id = id!!,
    bookId = bookId,
    instant = instant.toInstant()
)