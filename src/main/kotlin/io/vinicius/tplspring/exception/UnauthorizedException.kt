package io.vinicius.tplspring.exception

import io.vinicius.tplspring.model.Error
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException

class UnauthorizedException(
    val status: HttpStatus = HttpStatus.UNAUTHORIZED,
    type: String = "UNAUTHORIZED",
    title: String? = "Unauthorized",
    detail: String? = null,
    instance: String? = null,
) : AuthenticationException(type) {
    val body = Error(status.value(), type, title, detail, instance)
}