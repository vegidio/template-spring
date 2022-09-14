package io.vinicius.tplspring.exception

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

open class HttpException(
    val type: String,
    val status: HttpStatus,
    val title: String? = null,
    val detail: String? = null,
    val instance: String? = null
) : RuntimeException()

/**
 * https://www.rfc-editor.org/rfc/rfc7807
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class HttpExceptionBody(
    val type: String,
    val status: Int,
    val title: String? = null,
    val detail: String? = null,
    val instance: String? = null
)