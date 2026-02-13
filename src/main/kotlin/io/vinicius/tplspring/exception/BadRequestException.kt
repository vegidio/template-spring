package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.net.URI

/**
 * Modern exception using Spring's ErrorResponseException.
 * Directly extends ErrorResponseException following Spring community standards.
 */
class BadRequestException(
    detail: String,
    type: String = "BAD_REQUEST",
    title: String = "Bad Request",
) : ErrorResponseException(
        HttpStatus.BAD_REQUEST,
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail).apply {
            this.type = URI.create("https://api.errors/$type")
            this.title = title
        },
        null,
    )