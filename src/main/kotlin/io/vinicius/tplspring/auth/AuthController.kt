package io.vinicius.tplspring.auth

import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("v1/auth")
class AuthController(private val encoder: JwtEncoder) {

    @GetMapping("signin")
    fun findAll(): String {
        val asd = JwtClaimsSet.builder()
            .subject("vinicius")
            .claim("coco", "buceta")
            .build()

        return encoder.encode(JwtEncoderParameters.from(asd)).tokenValue
    }
}