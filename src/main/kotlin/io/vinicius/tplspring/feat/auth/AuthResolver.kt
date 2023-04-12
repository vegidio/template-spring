package io.vinicius.tplspring.feat.auth

import io.vinicius.tplspring.feat.auth.dto.SignInRequestDto
import io.vinicius.tplspring.feat.auth.dto.TokenResponseDto
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.security.Principal
import javax.validation.Valid

@Controller
class AuthResolver(private val authService: AuthService) {

    @MutationMapping(name = "signIn")
    fun signIn(@Valid @Argument dto: SignInRequestDto): TokenResponseDto =
        authService.signIn(dto.email, dto.password)

    @PreAuthorize("isAuthenticated()")
    @QueryMapping(name = "refresh")
    fun refresh(principal: Principal): TokenResponseDto =
        authService.refresh(principal.name.toInt())
}