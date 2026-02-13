package io.vinicius.tplspring.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.net.URI

/**
 * Modern exception using Spring's ErrorResponseException.
 * Directly extends ErrorResponseException following Spring community standards.
 */
class ServerErrorException(
    detail: String,
    type: String = "INTERNAL_SERVER_ERROR",
    title: String = "Internal Server Error",
) : ErrorResponseException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, detail).apply {
            this.type = URI.create("https://api.errors/$type")
            this.title = title
        },
        null,
    )