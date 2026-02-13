package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import java.net.URI

/**
 * Unauthorized exception for Spring Security.
 * Must extend AuthenticationException (not ErrorResponseException) for Spring Security integration.
 * Contains ProblemDetail for consistent error responses.
 */
class UnauthorizedException(
    detail: String = "Unauthorized",
    type: String = "UNAUTHORIZED",
    title: String = "Unauthorized",
) : AuthenticationException(detail) {
    val problemDetail: ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, detail).apply {
            this.type = URI.create("https://api.errors/$type")
            this.title = title
        }
}