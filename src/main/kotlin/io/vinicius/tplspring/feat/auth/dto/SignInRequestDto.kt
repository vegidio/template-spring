package io.vinicius.tplspring.feat.auth.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class SignInRequestDto(
    @field:NotEmpty
    @field:Email
    val email: String,

    @field:NotEmpty
    val password: String
)