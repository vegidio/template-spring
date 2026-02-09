package io.vinicius.tplspring.exception

import io.vinicius.tplspring.ktx.capitalized
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

/**
 * Modern exception handler using RFC 9457 Problem Details for HTTP APIs.
 * This is the standardized way to handle errors in Spring Boot 4.
 */
@RestControllerAdvice
class RestExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(value = [HttpException::class])
    fun handleHttpException(ex: HttpException): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(ex.status, ex.body.detail ?: "")
        problem.title = ex.body.title
        problem.type = URI.create("https://api.errors/${ex.body.type}")
        return problem
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolation(ex: ConstraintViolationException): ProblemDetail {
        val violation = ex.constraintViolations.first()
        val detail = "${violation.propertyPath.last().name.capitalized()} ${violation.message}"
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail)
        problem.title = "The parameter '${violation.invalidValue}' is invalid"
        problem.type = URI.create("https://api.errors/validation-failed")
        problem.setProperty("invalidValue", violation.invalidValue)
        return problem
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ProblemDetail {
        val fieldError = ex.fieldErrors.first()
        val detail = "${fieldError.field.capitalized()} ${fieldError.defaultMessage}"
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail)
        problem.title = "Validation failed for '${fieldError.objectName.capitalized()}'"
        problem.type = URI.create("https://api.errors/validation-failed")
        problem.setProperty("field", fieldError.field)
        problem.setProperty("rejectedValue", fieldError.rejectedValue)
        return problem
    }

    @ExceptionHandler(
        value = [
            UnauthorizedException::class,
            InsufficientAuthenticationException::class,
            AccessDeniedException::class,
        ],
    )
    fun handleUnauthorized(ex: RuntimeException): ProblemDetail =
        if (ex is UnauthorizedException) {
            val problem = ProblemDetail.forStatusAndDetail(ex.status, ex.body.detail ?: "")
            problem.title = ex.body.title
            problem.type = URI.create("https://api.errors/${ex.body.type}")
            problem
        } else {
            val problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "The bearer token is invalid")
            problem.title = "Unauthorized"
            problem.type = URI.create("https://api.errors/JWT_INVALID")
            problem
        }

    @ExceptionHandler(value = [Exception::class])
    fun handleGenericException(ex: Exception): ProblemDetail {
        logger.error("Unexpected error occurred", ex)
        val problem =
            ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "For security reasons, check the server logs for detailed information",
            )
        problem.title = "Unexpected error"
        problem.type = URI.create("https://api.errors/internal-server-error")
        return problem
    }
}