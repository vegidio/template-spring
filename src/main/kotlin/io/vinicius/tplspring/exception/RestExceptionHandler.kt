package io.vinicius.tplspring.exception

import io.vinicius.tplspring.ktx.capitalize
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(value = [HttpException::class])
    fun handleApiException(ex: HttpException): ResponseEntity<HttpException.Body> {
        return ResponseEntity(ex.body, ex.status)
    }

    // region - HTTP 400 Bad Request
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleApiException(ex: ConstraintViolationException): ResponseEntity<HttpException.Body> {
        val title = with(ex.constraintViolations.first()) { "The parameter '$invalidValue' is invalid" }
        val detail = with(ex.constraintViolations.first()) { "${propertyPath.last().name.capitalize()} $message" }
        val exception = BadRequestException(title = title, detail = detail)
        return ResponseEntity(exception.body, exception.status)
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleApiException(ex: MethodArgumentNotValidException): ResponseEntity<HttpException.Body> {
        val title = with(ex.fieldErrors.first()) { "Validation failed for '${objectName.capitalize()}'" }
        val detail = with(ex.fieldErrors.first()) { "${field.capitalize()} $defaultMessage" }
        val exception = BadRequestException(title = title, detail = detail)
        return ResponseEntity(exception.body, exception.status)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleApiException(ex: HttpMessageNotReadableException): ResponseEntity<HttpException.Body> {
        val title = "The request is incomplete or malformed."
        val detail = ex.cause?.message
        val exception = BadRequestException(title = title, detail = detail)
        return ResponseEntity(exception.body, exception.status)
    }
    // endregion

    // region - HTTP 401 Unauthorized
    @ExceptionHandler(value = [UnauthorizedException::class])
    fun handleApiException(ex: UnauthorizedException): ResponseEntity<HttpException.Body> {
        return ResponseEntity(ex.body, ex.status)
    }
    // endregion

    // region - HTTP 500 Internal Server Error
    @ExceptionHandler(value = [Exception::class])
    fun handleApiException(ex: Exception): ResponseEntity<HttpException.Body> {
        val title = "Unexpected error"
        val detail = ex.message
        val exception = ServerErrorException(title = title, detail = detail)
        return ResponseEntity(exception.body, exception.status)
    }
    // endregion
}