package io.vinicius.tplspring.domain.auth.dto

@JvmRecord
data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)