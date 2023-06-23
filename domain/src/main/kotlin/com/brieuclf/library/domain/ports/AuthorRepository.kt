package com.brieuclf.library.domain.ports

import com.brieuclf.library.domain.model.Author
import com.brieuclf.library.domain.model.CreateAuthorInformation
import java.util.UUID
import kotlinx.coroutines.flow.Flow

interface AuthorRepository {

    suspend fun findById(id: UUID): Author?
    suspend fun create(createAuthorInformation: CreateAuthorInformation): Author
    suspend fun findAll(): Flow<Author>
}