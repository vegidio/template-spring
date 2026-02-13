package io.vinicius.tplspring.domain.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.vinicius.tplspring.domain.auth.dto.SignInRequestDto
import io.vinicius.tplspring.domain.auth.dto.TokenResponseDto
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("\${apiPrefix}/v1/auth")
@Tag(name = "Auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("signin")
    fun signIn(
        @Valid @RequestBody dto: SignInRequestDto,
    ): TokenResponseDto = authService.signIn(dto.email, dto.password)

    @PreAuthorize("isAuthenticated()")
    @GetMapping("refresh")
    @Operation(security = [SecurityRequirement(name = "refresh-token")])
    fun refresh(principal: Principal): TokenResponseDto = authService.refresh(principal.name.toInt())
}