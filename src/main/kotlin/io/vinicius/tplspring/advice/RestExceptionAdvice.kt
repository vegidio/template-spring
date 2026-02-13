package io.vinicius.tplspring.advice

import io.vinicius.tplspring.exception.UnauthorizedException
import io.vinicius.tplspring.ktx.capitalized
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.web.ErrorResponseException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
class RestExceptionAdvice {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ErrorResponseException::class)
    fun handleErrorResponse(ex: ErrorResponseException): ProblemDetail? = ex.body

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ProblemDetail {
        val violation = ex.constraintViolations.first()
        val propertyName =
            violation.propertyPath
                .last()
                .name
                .capitalized()

        return ProblemDetail
            .forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "$propertyName ${violation.message}",
            ).apply {
                title = "The parameter '${violation.invalidValue}' is invalid"
                type = URI.create("https://api.errors/validation-failed")
                setProperty("invalidValue", violation.invalidValue)
            }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ProblemDetail {
        val fieldError = ex.fieldErrors.first()

        return ProblemDetail
            .forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "${fieldError.field.capitalized()} ${fieldError.defaultMessage}",
            ).apply {
                title = "Validation failed for '${fieldError.objectName.capitalized()}'"
                type = URI.create("https://api.errors/validation-failed")
                setProperty("field", fieldError.field)
                setProperty("rejectedValue", fieldError.rejectedValue)
            }
    }

    /**
     * Handles authentication and authorization exceptions.
     * Translates JWT exceptions (expired, invalid) to appropriate UnauthorizedException.
     * This is the recommended Spring approach - separation of concerns between JWT decoding and exception handling.
     */
    @ExceptionHandler(
        AccessDeniedException::class,
        InsufficientAuthenticationException::class,
        InvalidBearerTokenException::class,
        UnauthorizedException::class,
    )
    fun handleUnauthorized(ex: RuntimeException): ProblemDetail {
        logger.debug("Authentication/Authorization failed", ex)

        return when (ex) {
            is UnauthorizedException -> ex.problemDetail
            else -> {
                val jwtException = findJwtException(ex)
                val isExpired = jwtException?.message?.contains("expired", ignoreCase = true) == true
                val errorType = if (isExpired) "JWT_EXPIRED" else "JWT_INVALID"

                ProblemDetail
                    .forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        if (isExpired) "The bearer token expired" else "The bearer token is invalid",
                    ).apply {
                        title = "Unauthorized"
                        type = URI.create("https://api.errors/$errorType")
                    }
            }
        }
    }

    /**
     * Recursively finds JwtException in the exception chain.
     */
    private fun findJwtException(ex: Throwable?): JwtException? =
        ex?.let { current ->
            current as? JwtException ?: findJwtException(current.cause)
        }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ProblemDetail {
        logger.error("Unexpected error occurred", ex)

        return ProblemDetail
            .forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "For security reasons, check the server logs for detailed information",
            ).apply {
                title = "Unexpected error"
                type = URI.create("https://api.errors/internal-server-error")
            }
    }
}