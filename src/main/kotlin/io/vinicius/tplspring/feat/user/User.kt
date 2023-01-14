package io.vinicius.tplspring.feat.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import io.swagger.v3.oas.annotations.media.Schema
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id @Generated(GenerationTime.INSERT)
    val id: Int,

    val name: String,
    val username: String,
    val email: String,

    @Schema(hidden = true)
    @JsonProperty(access = Access.WRITE_ONLY)
    val hash: String
)