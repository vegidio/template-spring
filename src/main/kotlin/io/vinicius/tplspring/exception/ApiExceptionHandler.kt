package io.vinicius.tplspring.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(value = [HttpException::class])
    fun handleApiException(ex: HttpException): ResponseEntity<HttpExceptionBody> {
        val body = HttpExceptionBody(ex.type, ex.status.value(), ex.title, ex.detail, ex.instance)
        return ResponseEntity(body, ex.status)
    }
}