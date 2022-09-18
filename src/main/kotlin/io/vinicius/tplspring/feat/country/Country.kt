package io.vinicius.tplspring.feat.country

import com.fasterxml.jackson.annotation.JsonInclude
import org.hibernate.annotations.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "countries")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Country(
    @Id
    val code: String,

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
    val name: Name,

    @Column(nullable = true)
    val capital: String?,

    val region: String,

    @Column(name = "sub_region", nullable = true)
    val subRegion: String?,

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
    val languages: List<Language>,

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
    val currencies: List<Currency>,

    val population: Int,
    val area: Float,

    @Type(type = "list-array")
    @Column(columnDefinition = "real[]")
    val coordinates: List<Float>,

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
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