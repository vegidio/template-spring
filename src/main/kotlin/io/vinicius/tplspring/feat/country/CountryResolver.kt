package io.vinicius.tplspring.feat.country

import io.vinicius.tplspring.feat.country.converter.CountryCode
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import javax.validation.constraints.Size

@PreAuthorize("isAuthenticated()")
@Controller
class CountryResolver(private val countryService: CountryService) {

    @QueryMapping(name = "countries")
    fun findAll(): List<Country> {
        return countryService.findAll()
    }

    @QueryMapping(name = "country")
    fun findByCode(
        @Argument
        @Size(min = 3, max = 3, message = "must be 3 characters long")
        @CountryCode code: String
    ): Country {
        return countryService.findByCode(code)
    }
}