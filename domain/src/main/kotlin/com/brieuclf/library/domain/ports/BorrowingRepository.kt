package com.brieuclf.library.domain.ports

import com.brieuclf.library.domain.model.BorrowBookInformation
import com.brieuclf.library.domain.model.Borrowing

interface BorrowingRepository {

    suspend fun create(borrowBookInformation: BorrowBookInformation): Borrowing
}