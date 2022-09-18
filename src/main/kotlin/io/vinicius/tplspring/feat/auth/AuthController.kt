package io.vinicius.tplspring.feat.auth

import io.vinicius.tplspring.feat.auth.dto.SignInRequestDto
import io.vinicius.tplspring.feat.auth.dto.SignInResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("signin")
    fun signIn(@Valid @RequestBody dto: SignInRequestDto): SignInResponseDto {
        return authService.signIn(dto.email, dto.password)
    }
}