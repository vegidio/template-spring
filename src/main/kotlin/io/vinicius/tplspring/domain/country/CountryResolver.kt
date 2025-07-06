package io.vinicius.tplspring.domain.country

import jakarta.validation.constraints.Size
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller

@PreAuthorize("isAuthenticated()")
@Controller
class CountryResolver(private val countryService: CountryService) {

    @QueryMapping(name = "countries")
    fun findAll(): List<Country> =
        countryService.findAll()

    @QueryMapping(name = "country")
    fun findByCode(
        @Argument
        @Size(min = 3, max = 3, message = "must be 3 characters long")
        code: String
    ): Country =
        countryService.findByCode(code.uppercase())
}