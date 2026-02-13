package io.vinicius.tplspring.domain.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

@JvmRecord
data class SignInRequestDto(
    @field:NotEmpty
    @field:Email
    val email: String,

    @field:NotEmpty
    val password: String,
)