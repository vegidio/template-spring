package io.vinicius.tplspring.model

import com.fasterxml.jackson.annotation.JsonInclude

@JvmRecord
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response<T>(
    val data: T? = null,
)