package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus

class UnauthorizedException(
    status: HttpStatus = HttpStatus.UNAUTHORIZED,
    type: String = "UNAUTHORIZED",
    title: String? = "Unauthorized",
    detail: String? = null,
    instance: String? = null
) : HttpException(status, type, title, detail, instance)