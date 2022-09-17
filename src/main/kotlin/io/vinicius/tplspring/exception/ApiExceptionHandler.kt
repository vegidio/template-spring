package io.vinicius.tplspring.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(value = [HttpException::class])
    fun handleApiException(ex: HttpException): ResponseEntity<HttpException.Body> {
        return ResponseEntity(ex.body, ex.status)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleApiException(ex: ConstraintViolationException): ResponseEntity<HttpException.Body> {
        val detail = ex.constraintViolations.stream().findFirst().get().message
        val exception = BadRequestException(detail = detail)
        return ResponseEntity(exception.body, exception.status)
    }
}