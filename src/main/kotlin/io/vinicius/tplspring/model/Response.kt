package io.vinicius.tplspring.model

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response<T>(
    val data: T? = null,

    @Schema(hidden = true)
    val error: Error? = null
)