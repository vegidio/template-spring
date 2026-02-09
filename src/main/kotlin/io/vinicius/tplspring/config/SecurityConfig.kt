package io.vinicius.tplspring.config

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import io.vinicius.tplspring.exception.UnauthorizedException
import io.vinicius.tplspring.ktx.isFresh
import io.vinicius.tplspring.ktx.toJwt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.HandlerExceptionResolver
import java.text.ParseException

/**
 * Modern Spring Security 6+ configuration for Spring Boot 4.
 * Uses declarative security with method-level @PreAuthorize annotations.
 */
@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val certProperties: CertProperties,
    @param:Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() } // Stateless REST APIs are not susceptible to CSRF attacks
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { authorize ->
                authorize
                    // Allow public access to actuator health endpoints (for K8s probes)
                    .requestMatchers("/actuator/health/**")
                    .permitAll()
                    // Allow public access to OpenAPI documentation
                    .requestMatchers("/docs/**", "/v3/api-docs/**")
                    .permitAll()
                    // All other requests use method-level security
                    .anyRequest()
                    .permitAll()
            }.oauth2ResourceServer { oauth2 ->
                oauth2
                    .authenticationEntryPoint(authEntryPoint())
                    .jwt(Customizer.withDefaults())
            }.httpBasic(Customizer.withDefaults())
            .build()

    private fun authEntryPoint() =
        AuthenticationEntryPoint { request, response, authException ->
            logger.warn("Authentication failed for request to {}: {}", request.requestURI, authException.message)
            resolver.resolveException(request, response, null, authException)
        }

    @Bean
    fun jwtEncoder() =
        JwtEncoder { params ->
            val header =
                JWSHeader
                    .Builder(JWSAlgorithm.ES256)
                    .keyID(certProperties.accessTokenPrivate.keyID)
                    .type(JOSEObjectType.JWT)
                    .build()

            val payload = JWTClaimsSet.parse(params.claims.claims)

            val signedJwt = SignedJWT(header, payload)
            signedJwt.sign(ECDSASigner(certProperties.accessTokenPrivate))
            signedJwt.toJwt()
        }

    @Bean
    fun jwtDecoder() =
        JwtDecoder { token ->
            val signedJwt =
                try {
                    SignedJWT.parse(token)
                } catch (ex: ParseException) {
                    logger.debug("Failed to parse JWT token", ex)
                    null
                }
            val isValid = signedJwt?.verify(ECDSAVerifier(certProperties.accessTokenPublic))

            if (isValid != true) {
                throw UnauthorizedException(
                    type = "JWT_INVALID",
                    detail = "The bearer token is invalid",
                )
            }

            if (!signedJwt.isFresh()) {
                throw UnauthorizedException(
                    type = "JWT_EXPIRED",
                    detail = "The bearer token expired",
                )
            }

            signedJwt.toJwt()
        }

    @Bean
    fun argon2(): Argon2PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
}