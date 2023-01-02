package io.vinicius.tplspring.feat.auth.dto

data class TokenResponseDto(
    val accessToken: String,
    val refreshToken: String
)