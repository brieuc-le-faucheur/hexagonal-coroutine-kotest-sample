package com.brieuclf.library.domain.services

import com.brieuclf.library.domain.model.CreateAuthorInformation
import com.brieuclf.library.domain.ports.AuthorRepository
import java.util.UUID

class AuthorService(private val authorRepository: AuthorRepository) {

    suspend fun createAuthor(createAuthorInformation: CreateAuthorInformation) = authorRepository.create(createAuthorInformation)

    suspend fun getAuthor(id: UUID) = authorRepository.findById(id)
    suspend fun getAuthors() = authorRepository.findAll()
}