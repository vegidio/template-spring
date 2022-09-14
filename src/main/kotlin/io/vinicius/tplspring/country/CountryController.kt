package io.vinicius.tplspring.country

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Size

@Validated
@RestController
@RequestMapping("v1/countries")
class CountryController(private val countryService: CountryService) {

    @GetMapping
    fun findAll(): List<Country> {
        return countryService.findAll()
    }

    @GetMapping("{code}")
    fun findByCode(@PathVariable("code") @Size(min = 3, max = 3) code: String): Country {
        return countryService.findByCode(code)
    }
}