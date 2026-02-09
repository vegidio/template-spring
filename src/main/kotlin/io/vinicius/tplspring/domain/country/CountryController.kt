package io.vinicius.tplspring.domain.country

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.vinicius.tplspring.domain.country.converter.CountryCode
import io.vinicius.tplspring.model.Response
import jakarta.validation.constraints.Size
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("isAuthenticated()")
@Validated
@RestController
@RequestMapping("\${apiPrefix}/v1/countries")
@Tag(name = "Country")
class CountryController(
    private val countryService: CountryService,
) {
    @GetMapping
    @Operation(security = [SecurityRequirement(name = "access-token")])
    fun findAll(): Response<List<Country>> {
        val data = countryService.findAll()
        return Response(data)
    }

    @GetMapping("{code}")
    @Operation(security = [SecurityRequirement(name = "access-token")])
    fun findByCode(
        @PathVariable
        @Size(min = 3, max = 3, message = "must be 3 characters long")
        @CountryCode code: String,
    ): Response<Country> {
        val data = countryService.findByCode(code)
        return Response(data)
    }
}