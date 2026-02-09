package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus

class ServerErrorException(
    status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    type: String = "INTERNAL_SERVER_ERROR",
    title: String? = "Internal Server Error",
    detail: String? = null,
    instance: String? = null,
) : HttpException(status, type, title, detail, instance)