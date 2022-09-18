package io.vinicius.tplspring.feat.auth

import io.vinicius.tplspring.exception.UnauthorizedException
import io.vinicius.tplspring.feat.auth.dto.SignInResponseDto
import io.vinicius.tplspring.feat.user.User
import io.vinicius.tplspring.feat.user.UserRepository
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepo: UserRepository,
    private val argon2: Argon2PasswordEncoder,
    private val encoder: JwtEncoder,
    private val decoder: JwtDecoder
) {
    fun signIn(email: String, password: String): SignInResponseDto {
        val user = userRepo.findByEmail(email) ?: throw UnauthorizedException(detail = "Invalid user credentials")
        val pwMatched = argon2.matches(password, user.hash)

        if (!pwMatched) throw UnauthorizedException(detail = "Invalid user credentials")
        return createToken(user)
    }

    // region - Private methods
    private fun createToken(user: User): SignInResponseDto {
        val payload = JwtEncoderParameters.from(
            JwtClaimsSet.builder()
                .subject(user.id.toString())
                .claim("username", user.username)
                .build()
        )

        return SignInResponseDto(
            accessToken = encoder.encode(payload).tokenValue,
            expires = 3600
        )
    }
    // endregion
}