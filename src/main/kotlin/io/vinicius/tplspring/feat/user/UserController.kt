package io.vinicius.tplspring.feat.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("v1/users")
@Tag(name = "User")
class UserController(private val userService: UserService) {

    @GetMapping("me")
    @Operation(security = [SecurityRequirement(name = "access-token")])
    fun findMe(principal: Principal): User {
        return userService.findById(principal.name.toInt())
    }
}