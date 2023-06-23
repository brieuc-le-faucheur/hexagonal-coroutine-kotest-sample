package com.brieuclf.library.infra.repository

import com.brieuclf.library.infra.entity.BorrowingEntity
import java.util.UUID
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface BorrowingCrudRepository : CoroutineCrudRepository<BorrowingEntity, UUID>, CoroutineSortingRepository<BorrowingEntity, UUID>
