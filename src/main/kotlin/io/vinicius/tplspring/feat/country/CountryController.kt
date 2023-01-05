package io.vinicius.tplspring.feat.country

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.vinicius.tplspring.feat.country.converter.CountryCode
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Size

@Validated
@RestController
@RequestMapping("v1/countries")
@Tag(name = "Country")
class CountryController(private val countryService: CountryService) {

    @GetMapping
    @Operation(security = [SecurityRequirement(name = "access-token")])
    fun findAll(): List<Country> {
        return countryService.findAll()
    }

    @GetMapping("{code}")
    @Operation(security = [SecurityRequirement(name = "access-token")])
    fun findByCode(
        @PathVariable
        @Size(min = 3, max = 3, message = "must be 3 characters long")
        @CountryCode code: String
    ): Country {
        return countryService.findByCode(code)
    }
}