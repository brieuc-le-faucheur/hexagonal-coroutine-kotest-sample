package com.brieuclf.library.infra.repository

import com.brieuclf.library.infra.entity.BookEntity
import java.util.UUID
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BookCrudRepository : CoroutineCrudRepository<BookEntity, UUID>, CoroutineSortingRepository<BookEntity, UUID>
