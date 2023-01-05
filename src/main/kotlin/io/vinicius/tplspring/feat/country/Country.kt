package io.vinicius.tplspring.feat.country

import com.fasterxml.jackson.annotation.JsonInclude
import io.hypersistence.utils.hibernate.type.array.ListArrayType
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "countries")
@TypeDef(name = "list-array", typeClass = ListArrayType::class)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Country(
    @Id
    val code: String,

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    val name: Name,

    @Column(nullable = true)
    val capital: String?,

    val region: String,

    @Column(name = "sub_region", nullable = true)
    val subRegion: String?,

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    val languages: List<Language>,

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    val currencies: List<Currency>,

    val population: Int,
    val area: Float,

    // The @Type(parameters =) must be added because real / Float is no longer supported
    // https://github.com/vladmihalcea/hypersistence-utils/issues/500#issuecomment-1283670602
    @Type(type = "list-array", parameters = [Parameter(name = "sql_array_type", value = "real")])
    val coordinates: List<Float>,

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
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