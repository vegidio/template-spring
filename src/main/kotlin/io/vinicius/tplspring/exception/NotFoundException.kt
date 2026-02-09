package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    status: HttpStatus = HttpStatus.NOT_FOUND,
    type: String = "NOT_FOUND",
    title: String? = "Not Found",
    detail: String? = null,
    instance: String? = null,
) : HttpException(status, type, title, detail, instance)