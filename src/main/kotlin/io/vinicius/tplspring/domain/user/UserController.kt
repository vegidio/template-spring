package io.vinicius.tplspring.domain.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.vinicius.tplspring.model.Response
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("\${apiPrefix}/v1/users")
@Tag(name = "User")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("me")
    @Operation(security = [SecurityRequirement(name = "access-token")])
    fun findMe(principal: Principal): Response<User> {
        val data = userService.findById(principal.name.toInt())
        return Response(data)
    }
}