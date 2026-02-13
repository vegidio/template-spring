package io.vinicius.tplspring.domain.auth

import io.vinicius.tplspring.domain.auth.dto.TokenResponseDto
import io.vinicius.tplspring.domain.user.User
import io.vinicius.tplspring.domain.user.UserRepository
import io.vinicius.tplspring.exception.UnauthorizedException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class AuthService(
    private val userRepo: UserRepository,
    private val argon2: Argon2PasswordEncoder,
    @param:Qualifier("accessTokenEncoder") private val accessEncoder: JwtEncoder,
    @param:Qualifier("refreshTokenEncoder") private val refreshEncoder: JwtEncoder,
) {
    fun signIn(
        email: String,
        password: String,
    ): TokenResponseDto {
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

        return TokenResponseDto(
            accessToken =
                buildToken(accessEncoder, user, now, now.plusHours(1)) {
                    claim("username", user.username)
                },
            refreshToken = buildToken(refreshEncoder, user, now, now.plusDays(1)),
        )
    }

    private fun buildToken(
        encoder: JwtEncoder,
        user: User,
        issuedAt: OffsetDateTime,
        expiresAt: OffsetDateTime,
        extraClaims: JwtClaimsSet.Builder.() -> Unit = {},
    ): String {
        val headers = JwsHeader.with(SignatureAlgorithm.ES256).build()
        val claims =
            JwtClaimsSet
                .builder()
                .subject(user.id.toString())
                .issuedAt(issuedAt.toInstant())
                .expiresAt(expiresAt.toInstant())
                .apply(extraClaims)
                .build()

        return encoder.encode(JwtEncoderParameters.from(headers, claims)).tokenValue
    }
    // endregion
}