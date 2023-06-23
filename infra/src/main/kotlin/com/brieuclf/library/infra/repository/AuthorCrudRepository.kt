package com.brieuclf.library.infra.repository

import com.brieuclf.library.infra.entity.AuthorEntity
import java.util.UUID
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorCrudRepository : CoroutineCrudRepository<AuthorEntity, UUID>, CoroutineSortingRepository<AuthorEntity, UUID>
