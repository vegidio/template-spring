package io.vinicius.tplspring.exception

import io.vinicius.tplspring.model.Error
import org.springframework.http.HttpStatus

open class HttpException(
    val status: HttpStatus,
    type: String,
    title: String? = null,
    detail: String? = null,
    instance: String? = null
) : RuntimeException() {
    val body = Error(status.value(), type, title, detail, instance)
}