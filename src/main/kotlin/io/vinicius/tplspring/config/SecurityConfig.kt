package io.vinicius.tplspring.config

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import io.vinicius.tplspring.exception.UnauthorizedException
import io.vinicius.tplspring.ktx.date
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.HandlerExceptionResolver
import java.time.LocalDateTime

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val certProperties: CertProperties,
    @Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .authorizeRequests {
                it.antMatchers("/v1/countries/**").authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .oauth2ResourceServer { it.authenticationEntryPoint(authEntryPoint()).jwt() }
            .httpBasic(Customizer.withDefaults())
            .build()
    }

    private fun authEntryPoint() = AuthenticationEntryPoint { request, response, authException ->
        resolver.resolveException(request, response, null, authException)
    }

    @Bean
    fun jwtEncoder() = JwtEncoder { params ->
        val header = JWSHeader.Builder(JWSAlgorithm.ES256)
            .keyID(certProperties.privateKey?.keyID)
            .type(JOSEObjectType.JWT)
            .build()

        val payload = JWTClaimsSet.Builder(JWTClaimsSet.parse(params.claims.claims))
            .issueTime(LocalDateTime.now().date)
            .expirationTime(LocalDateTime.now().plusHours(1).date)
            .build()

        val signedJwt = SignedJWT(header, payload)
        signedJwt.sign(ECDSASigner(certProperties.privateKey))

        Jwt(
            signedJwt.serialize(),
            signedJwt.jwtClaimsSet.issueTime.toInstant(),
            signedJwt.jwtClaimsSet.expirationTime.toInstant(),
            signedJwt.header.toJSONObject(),
            signedJwt.jwtClaimsSet.toJSONObject()
        )
    }

    @Bean
    fun jwtDecoder() = JwtDecoder { token ->
        val signedJwt = SignedJWT.parse(token)
        val isValid = signedJwt.verify(ECDSAVerifier(certProperties.publicKey))
        if (!isValid) throw UnauthorizedException(type = "AUTH_INVALID", detail = "The bearer token is invalid.")

        Jwt(
            token,
            signedJwt.jwtClaimsSet.issueTime.toInstant(),
            signedJwt.jwtClaimsSet.expirationTime.toInstant(),
            signedJwt.header.toJSONObject(),
            signedJwt.jwtClaimsSet.toJSONObject()
        )
    }

    @Bean
    fun argon2(): Argon2PasswordEncoder = Argon2PasswordEncoder()
}