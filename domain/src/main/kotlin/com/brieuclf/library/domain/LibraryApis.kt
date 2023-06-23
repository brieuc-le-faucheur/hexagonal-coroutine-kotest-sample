package com.brieuclf.library.domain

import com.brieuclf.library.domain.services.AuthorService
import com.brieuclf.library.domain.services.BookService

class LibraryApis(val authorService: AuthorService, val bookService: BookService)