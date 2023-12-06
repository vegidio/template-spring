package io.vinicius.tplspring.feat.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class SignInRequestDto(
    @field:NotEmpty
    @field:Email
    val email: String,

    @field:NotEmpty
    val password: String
)