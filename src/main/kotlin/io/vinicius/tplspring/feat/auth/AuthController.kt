package io.vinicius.tplspring.feat.auth

import io.swagger.v3.oas.annotations.tags.Tag
import io.vinicius.tplspring.feat.auth.dto.SignInRequestDto
import io.vinicius.tplspring.feat.auth.dto.TokenResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("v1/auth")
@Tag(name = "Auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("signin")
    fun signIn(@Valid @RequestBody dto: SignInRequestDto): TokenResponseDto {
        return authService.signIn(dto.email, dto.password)
    }

    @GetMapping("refresh")
    fun refresh(principal: Principal): TokenResponseDto {
        return authService.refresh(principal.name.toInt())
    }
}