package com.brieuclf.library.infra.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("books")
data class BookEntity(
    @Id val id: UUID? = null,
    @Column val title: String,
    @Column val authorId: UUID,
    @Column val isBorrowed: Boolean,
    @Column val borrowedInstant: String = ""
)
