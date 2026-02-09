package io.vinicius.tplspring.ktx

import com.nimbusds.jwt.SignedJWT
import org.springframework.security.oauth2.jwt.Jwt
import java.time.OffsetDateTime

fun SignedJWT.toJwt() = Jwt(
    this.serialize(),
    this.jwtClaimsSet.issueTime?.toInstant(),
    this.jwtClaimsSet.expirationTime?.toInstant(),
    this.header.toJSONObject(),
    this.jwtClaimsSet.toJSONObject()
)

fun SignedJWT.isFresh(): Boolean {
    val now = OffsetDateTime.now()
    val issued = this.jwtClaimsSet.issueTime?.toOffsetDateTime() ?: now
    val expiration = this.jwtClaimsSet.expirationTime?.toOffsetDateTime() ?: now.plusSeconds(1)

    return now in issued..<expiration
}