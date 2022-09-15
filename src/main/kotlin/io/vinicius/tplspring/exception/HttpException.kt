package io.vinicius.tplspring.exception

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus

open class HttpException(
    val status: HttpStatus,
    type: String,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : RuntimeException() {
    val body = HttpExceptionBody(status.value(), type, title, detail, instance)
}

/**
 * https://www.rfc-editor.org/rfc/rfc7807
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class HttpExceptionBody(
    val status: Int,
    val type: String,
    val title: String? = null,
    val detail: String? = null,
    val instance: String? = null
)