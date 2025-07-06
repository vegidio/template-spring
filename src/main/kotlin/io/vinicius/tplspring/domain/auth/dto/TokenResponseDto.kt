package io.vinicius.tplspring.domain.auth.dto

data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)