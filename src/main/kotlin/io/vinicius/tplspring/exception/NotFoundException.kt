package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.net.URI

/**
 * Modern exception using Spring's ErrorResponseException.
 * Directly extends ErrorResponseException following Spring community standards.
 */
class NotFoundException(
    detail: String,
    type: String = "NOT_FOUND",
    title: String = "Not Found",
) : ErrorResponseException(
        HttpStatus.NOT_FOUND,
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, detail).apply {
            this.type = URI.create("https://api.errors/$type")
            this.title = title
        },
        null,
    )