package io.vinicius.tplspring.feat.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.vinicius.tplspring.feat.auth.dto.SignInRequestDto
import io.vinicius.tplspring.feat.auth.dto.TokenResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("\${apiPrefix}/v1/auth")
@Tag(name = "Auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("signin")
    fun signIn(@Valid @RequestBody dto: SignInRequestDto): TokenResponseDto {
        return authService.signIn(dto.email, dto.password)
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("refresh")
    @Operation(security = [SecurityRequirement(name = "refresh-token")])
    fun refresh(principal: Principal): TokenResponseDto {
        return authService.refresh(principal.name.toInt())
    }
}