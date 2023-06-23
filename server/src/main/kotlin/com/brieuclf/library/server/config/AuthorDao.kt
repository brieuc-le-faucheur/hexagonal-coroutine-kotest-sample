package com.brieuclf.library.server.config

import com.brieuclf.library.domain.model.CreateAuthorInformation
import com.brieuclf.library.domain.ports.AuthorRepository
import com.brieuclf.library.infra.entity.AuthorEntity
import com.brieuclf.library.infra.repository.AuthorCrudRepository
import com.brieuclf.library.server.mappers.toDomain
import com.brieuclf.library.server.mappers.toEntity
import java.util.UUID
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class AuthorDao(private val authorRepository: AuthorCrudRepository) : AuthorRepository {
    override suspend fun findById(id: UUID) = authorRepository.findById(id)?.toDomain()
    override suspend fun create(createAuthorInformation: CreateAuthorInformation) =
        authorRepository.save(createAuthorInformation.toEntity()).toDomain()

    override suspend fun findAll() = authorRepository.findAll().map(AuthorEntity::toDomain)
}