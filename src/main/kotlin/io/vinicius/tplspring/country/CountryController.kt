package io.vinicius.tplspring.country

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("countries")
class CountryController(private val countryService: CountryService) {

    @GetMapping
    fun findAll(): List<Country> {
        return countryService.findAll()
    }

    @GetMapping("{code}")
    fun findByCode(@PathVariable code: String): Country {
        return countryService.findByCode(code)
    }
}