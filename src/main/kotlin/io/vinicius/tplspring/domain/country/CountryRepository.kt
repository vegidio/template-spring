package io.vinicius.tplspring.domain.country

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CountryRepository : JpaRepository<Country, String> {
    @Query("SELECT * FROM countries ORDER BY name->>'common' ASC", nativeQuery = true)
    fun findCountries(): List<Country>
}