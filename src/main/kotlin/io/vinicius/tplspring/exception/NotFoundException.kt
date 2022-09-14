package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    type: String = "NOT_FOUND",
    status: HttpStatus = HttpStatus.NOT_FOUND,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : HttpException(type, status, title, detail, instance)