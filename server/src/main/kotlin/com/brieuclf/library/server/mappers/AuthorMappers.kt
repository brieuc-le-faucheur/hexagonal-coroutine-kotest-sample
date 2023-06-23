package com.brieuclf.library.server.mappers

import com.brieuclf.library.domain.model.Author
import com.brieuclf.library.domain.model.CreateAuthorInformation
import com.brieuclf.library.infra.entity.AuthorEntity

fun AuthorEntity.toDomain() = Author(
    firstName = firstName,
    lastName = lastName,
    id = id!!
)

fun CreateAuthorInformation.toEntity() = AuthorEntity(
    firstName = firstName,
    lastName = lastName,
    id = null
)
