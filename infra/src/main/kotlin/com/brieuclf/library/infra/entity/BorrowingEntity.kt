package com.brieuclf.library.infra.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("borrowings")
data class BorrowingEntity(
    @Column val bookId: UUID,
    @Column val instant: String,
    @Id val id: UUID? = null
)