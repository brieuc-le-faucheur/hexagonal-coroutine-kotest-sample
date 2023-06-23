package com.brieuclf.library.server.commands

import java.util.UUID
import kotlin.time.Duration

data class BorrowBookCommand(val bookId: UUID, private val inputDuration: String) {
    val duration: Duration = Duration.parse(inputDuration)
}