package com.brieuclf.library.infra.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("authors")
data class AuthorEntity(
    @Column val firstName: String,
    @Column val lastName: String,
    @Id val id: UUID? = null
)