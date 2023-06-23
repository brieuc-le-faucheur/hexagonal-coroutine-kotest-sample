package com.brieuclf.library.server.config

import com.brieuclf.library.domain.model.BorrowBookInformation
import com.brieuclf.library.domain.ports.BorrowingRepository
import com.brieuclf.library.infra.repository.BorrowingCrudRepository
import com.brieuclf.library.server.mappers.toDomain
import com.brieuclf.library.server.mappers.toEntity
import org.springframework.stereotype.Component

@Component
class BorrowingDao(private val borrowingRepository: BorrowingCrudRepository) : BorrowingRepository {
    override suspend fun create(borrowBookInformation: BorrowBookInformation) =
        borrowingRepository.save(borrowBookInformation.toEntity()).toDomain()
}