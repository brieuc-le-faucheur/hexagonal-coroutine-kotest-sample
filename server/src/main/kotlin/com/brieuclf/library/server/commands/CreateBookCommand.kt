package com.brieuclf.library.server.commands

import java.util.UUID

data class CreateBookCommand(val title: String, val authorId: UUID)