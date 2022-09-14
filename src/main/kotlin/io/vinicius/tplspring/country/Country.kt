package io.vinicius.tplspring.country

import org.hibernate.annotations.Type
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "countries")
data class Country(
    @Id
    val code: String,

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
    val name: CountryName
)

data class CountryName(
    val common: String,
    val official: String,
    val nativeName: List<CountryNativeName>
)

data class CountryNativeName(
    val language: String,
    val common: String,
    val official: String
)