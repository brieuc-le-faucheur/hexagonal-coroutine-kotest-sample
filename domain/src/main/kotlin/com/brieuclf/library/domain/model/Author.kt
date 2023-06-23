package com.brieuclf.library.domain.model

import java.util.UUID

data class Author(
    val lastName: String,
    val firstName: String,
    val id: UUID
)

data class CreateAuthorInformation(
    val lastName: String,
    val firstName: String
)