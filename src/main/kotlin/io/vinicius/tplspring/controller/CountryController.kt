package io.vinicius.tplspring.controller

import io.vinicius.tplspring.model.Country
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CountryController {
    @GetMapping("/country")
    fun fetchCountryById(
        @RequestParam(required = false) id: String?
    ): Country {
        return Country("Brazil")
    }
}