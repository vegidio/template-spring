package io.vinicius.tplspring.feat.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.vinicius.tplspring.feat.auth.dto.SignInRequestDto
import io.vinicius.tplspring.feat.auth.dto.TokenResponseDto
import io.vinicius.tplspring.model.Response
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
class AuthController(private val authService: AuthService) {

    @PostMapping("signin")
    fun signIn(@Valid @RequestBody dto: SignInRequestDto): Response<TokenResponseDto> {
        val data = authService.signIn(dto.email, dto.password)
        return Response(data)
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("refresh")
    @Operation(security = [SecurityRequirement(name = "refresh-token")])
    fun refresh(principal: Principal): Response<TokenResponseDto> {
        val data = authService.refresh(principal.name.toInt())
        return Response(data)
    }
}