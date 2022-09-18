package io.vinicius.tplspring.feat.auth.dto

data class SignInResponseDto(
    val accessToken: String,
    val expires: Int
)