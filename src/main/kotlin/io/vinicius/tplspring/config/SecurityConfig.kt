package io.vinicius.tplspring.config

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
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
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.HandlerExceptionResolver

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

    /**
     * Security filter chain for refresh token endpoints.
     * This chain has a higher priority (order 1) and uses the refresh token decoder.
     */
    @Bean
    fun refreshTokenSecurityFilterChain(
        http: HttpSecurity,
        @Qualifier("refreshTokenDecoder") jwtDecoder: JwtDecoder,
    ): SecurityFilterChain =
        http
            .securityMatcher("/**/auth/refresh")
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .oauth2ResourceServer {
                it
                    .authenticationEntryPoint(authEntryPoint())
                    .jwt { jwt -> jwt.decoder(jwtDecoder) }
            }.httpBasic(Customizer.withDefaults())
            .build()

    /**
     * Default security filter chain for all other endpoints.
     * This chain uses the access token decoder.
     */
    @Bean
    fun accessTokenSecurityFilterChain(
        http: HttpSecurity,
        @Qualifier("accessTokenDecoder") jwtDecoder: JwtDecoder,
    ): SecurityFilterChain =
        http
            .csrf { it.disable() } // Stateless REST APIs are not susceptible to CSRF attacks
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    // Allow public access to actuator health endpoints (for K8s probes)
                    .requestMatchers("/actuator/health/**")
                    .permitAll()
                    // Allow public access to OpenAPI documentation
                    .requestMatchers("/docs/**", "/v3/api-docs/**")
                    .permitAll()
                    // All other requests use method-level security
                    .anyRequest()
                    .permitAll()
            }.oauth2ResourceServer {
                it
                    .authenticationEntryPoint(authEntryPoint())
                    .jwt { jwt -> jwt.decoder(jwtDecoder) }
            }.httpBasic(Customizer.withDefaults())
            .build()

    /**
     * JWT encoder using private key for signing tokens.
     */
    @Bean
    @Qualifier("accessTokenEncoder")
    fun jwtEncoder(): JwtEncoder = createJwtEncoder(certProperties.accessTokenPrivate)

    /**
     * JWT encoder using private key for signing refresh tokens.
     */
    @Bean
    @Qualifier("refreshTokenEncoder")
    fun refreshJwtEncoder(): JwtEncoder = createJwtEncoder(certProperties.refreshTokenPrivate)

    /**
     * JWT decoder using public key for validating tokens.
     * Exception translation is handled by @RestControllerAdvice.
     */
    @Bean
    @Qualifier("accessTokenDecoder")
    fun jwtDecoder(): JwtDecoder = createJwtDecoder(certProperties.accessTokenPublic)

    /**
     * JWT decoder using public key for validating refresh tokens.
     * Exception translation is handled by @RestControllerAdvice.
     */
    @Bean
    @Qualifier("refreshTokenDecoder")
    fun refreshJwtDecoder(): JwtDecoder = createJwtDecoder(certProperties.refreshTokenPublic)

    @Bean
    fun argon2(): Argon2PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    // region - Private methods
    private fun authEntryPoint() =
        AuthenticationEntryPoint { request, response, authException ->
            logger.warn("Authentication failed for request to {}: {}", request.requestURI, authException.message)
            resolver.resolveException(request, response, null, authException)
        }

    private fun createJwtEncoder(jwk: JWK): JwtEncoder {
        val jwtSet = JWKSet(jwk)
        val jwkSource = ImmutableJWKSet<SecurityContext>(jwtSet)
        return NimbusJwtEncoder(jwkSource)
    }

    private fun createJwtDecoder(jwk: JWK): JwtDecoder {
        val jwtSet = JWKSet(jwk)
        val jwkSource = ImmutableJWKSet<SecurityContext>(jwtSet)
        val jwtProcessor = DefaultJWTProcessor<SecurityContext>().apply {
            jwsKeySelector = JWSVerificationKeySelector(JWSAlgorithm.ES256, jwkSource)
        }

        return NimbusJwtDecoder(jwtProcessor)
    }
    // endregion
}