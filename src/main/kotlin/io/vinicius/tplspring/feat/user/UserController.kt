package io.vinicius.tplspring.feat.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("v1/users")
class UserController(private val userService: UserService) {

    @GetMapping("me")
    fun findMe(principal: Principal): User {
        return userService.findById(principal.name.toInt())
    }
}