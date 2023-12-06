package io.vinicius.tplspring.feat.country

import com.fasterxml.jackson.annotation.JsonInclude
import io.hypersistence.utils.hibernate.type.array.ListArrayType
import io.hypersistence.utils.hibernate.type.json.JsonType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.Type

@Entity
@Table(name = "countries")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Country(
    @Id
    val code: String,

    @Type(JsonType::class)
    val name: Name,

    @Column(nullable = true)
    val capital: String?,

    val region: String,

    @Column(name = "sub_region", nullable = true)
    val subRegion: String?,

    @Type(JsonType::class)
    val languages: List<Language>,

    @Type(JsonType::class)
    val currencies: List<Currency>,

    val population: Int,
    val area: Float,

    // The @Type(parameters =) must be added because real / Float is no longer supported
    // https://github.com/vladmihalcea/hypersistence-utils/issues/500#issuecomment-1283670602
    @Type(value = ListArrayType::class, parameters = [Parameter(name = ListArrayType.SQL_ARRAY_TYPE, value = "real")])
    val coordinates: List<Float>,

    @Type(JsonType::class)
    val flags: Flag
) {
    data class Name(
        val common: String,
        val official: String,
        val nativeName: List<NativeName>
    )

    data class NativeName(
        val language: String,
        val common: String,
        val official: String
    )

    data class Language(
        val code: String,
        val name: String
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Currency(
        val code: String,
        val name: String,
        val symbol: String?
    )

    data class Flag(
        val png: String,
        val svg: String
    )
}