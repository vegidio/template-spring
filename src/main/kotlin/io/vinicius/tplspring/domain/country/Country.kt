package io.vinicius.tplspring.domain.country

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "countries")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Country(
    @Id
    val code: String,

    @JdbcTypeCode(SqlTypes.JSON)
    val name: Name,

    @Column(nullable = true)
    val capital: String?,

    val region: String,

    @Column(name = "sub_region", nullable = true)
    val subRegion: String?,

    @JdbcTypeCode(SqlTypes.JSON)
    val languages: List<Language>,

    @JdbcTypeCode(SqlTypes.JSON)
    val currencies: List<Currency>,

    val population: Int,

    val area: Float,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "real[]")
    val coordinates: List<Float>,

    @JdbcTypeCode(SqlTypes.JSON)
    val flags: Flag,
) {
    data class Name(
        val common: String,
        val official: String,
        val nativeName: List<NativeName>,
    )

    data class NativeName(
        val language: String,
        val common: String,
        val official: String,
    )

    data class Language(
        val code: String,
        val name: String,
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Currency(
        val code: String,
        val name: String,
        val symbol: String?,
    )

    data class Flag(
        val png: String,
        val svg: String,
    )
}