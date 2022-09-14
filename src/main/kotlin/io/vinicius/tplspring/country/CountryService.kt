package io.vinicius.tplspring.country

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CountryService(private val countryRepository: CountryRepository) {
    fun findAll(): List<Country> {
        return countryRepository.findCountries()
    }

    fun findByCode(code: String): Country {
        return countryRepository.findByIdOrNull(code)!!
    }
}