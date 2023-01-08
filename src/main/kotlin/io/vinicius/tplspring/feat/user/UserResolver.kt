package io.vinicius.tplspring.feat.user

import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class UserResolver(private val userService: UserService) {

    @PreAuthorize("isAuthenticated()")
    @QueryMapping(name = "me")
    fun findMe(principal: Principal): User {
        return userService.findById(principal.name.toInt())
    }
}