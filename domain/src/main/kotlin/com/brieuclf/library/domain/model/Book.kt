package com.brieuclf.library.domain.model

import java.util.UUID
import kotlinx.datetime.Instant

data class Book(
    val title: String,
    val authorId: UUID,
    val id: UUID,
    val borrowing: Borrowed = Borrowed()
)

data class Borrowed(
    val isBorrowed: Boolean = false,
    val borrowedInstant: Instant? = null,
)

data class CreateBookInformation(
    val title: String,
    val authorId: UUID
)
