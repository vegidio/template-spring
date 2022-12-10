package io.vinicius.tplspring.feat.country

import io.vinicius.tplspring.exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CountryService(private val countryRepo: CountryRepository) {
    fun findAll(): List<Country> {
        return countryRepo.findCountries()
    }

    fun findByCode(code: String): Country {
        return countryRepo.findByIdOrNull(code) ?: throw NotFoundException(detail = "No country found with this code")
    }
}