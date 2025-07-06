package io.vinicius.tplspring.domain.auth

import io.vinicius.tplspring.exception.UnauthorizedException
import io.vinicius.tplspring.domain.auth.dto.TokenResponseDto
import io.vinicius.tplspring.domain.user.User
import io.vinicius.tplspring.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class AuthService(
    private val userRepo: UserRepository,
    private val argon2: Argon2PasswordEncoder,
    private val encoder: JwtEncoder
) {
    fun signIn(email: String, password: String): TokenResponseDto {
        val user = userRepo.findByEmail(email) ?: throw UnauthorizedException(detail = "Invalid user credentials")
        val pwMatched = argon2.matches(password, user.hash)

        if (!pwMatched) throw UnauthorizedException(detail = "Invalid user credentials")
        return createToken(user)
    }

    fun refresh(userId: Int): TokenResponseDto {
        val user = userRepo.findByIdOrNull(userId) ?: throw UnauthorizedException(detail = "Invalid user credentials")
        return createToken(user)
    }

    // region - Private methods
    private fun createToken(user: User): TokenResponseDto {
        val now = OffsetDateTime.now()

        val accessPayload = JwtEncoderParameters.from(
            JwtClaimsSet.builder()
                .subject(user.id.toString())
                .claim("iat", now.toEpochSecond())
                .claim("exp", now.plusHours(1).toEpochSecond())
                .claim("username", user.username)
                .build()
        )

        val refreshPayload = JwtEncoderParameters.from(
            JwtClaimsSet.builder()
                .subject(user.id.toString())
                .claim("iat", now.toEpochSecond())
                .claim("exp", now.plusDays(1).toEpochSecond())
                .build()
        )

        return TokenResponseDto(
            accessToken = encoder.encode(accessPayload).tokenValue,
            refreshToken = encoder.encode(refreshPayload).tokenValue
        )
    }
    // endregion
}