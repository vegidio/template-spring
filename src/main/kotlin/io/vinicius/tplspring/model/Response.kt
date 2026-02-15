package io.vinicius.tplspring.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.ProblemDetail

/**
 * Unified response wrapper for both successful and error responses.
 *
 * - Successful responses use the 'data' field
 * - Error responses use the 'error' field
 */
@JvmRecord
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response<T>(
    val data: T? = null,

    val error: ProblemDetail? = null,
)