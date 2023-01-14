package io.vinicius.tplspring.model

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * https://www.rfc-editor.org/rfc/rfc7807
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Error(
    val status: Int,
    val type: String,
    val title: String? = null,
    val detail: String? = null,
    val instance: String? = null
)