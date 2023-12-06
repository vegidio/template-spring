package io.vinicius.tplspring.feat.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Generated
import org.hibernate.generator.EventType

@Entity
@Table(name = "users")
data class User(
    @Id @Generated(event = [EventType.INSERT])
    val id: Int,

    val name: String,
    val username: String,
    val email: String,

    @Schema(hidden = true)
    @JsonProperty(access = Access.WRITE_ONLY)
    val hash: String
)