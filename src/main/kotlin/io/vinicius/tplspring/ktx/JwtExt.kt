package io.vinicius.tplspring.ktx

import com.nimbusds.jwt.SignedJWT
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

fun SignedJWT.toJwt() =
    Jwt(
        this.serialize(),
        this.jwtClaimsSet.issueTime?.toInstant(),
        this.jwtClaimsSet.expirationTime?.toInstant(),
        this.header.toJSONObject(),
        this.jwtClaimsSet.toJSONObject(),
    )

fun SignedJWT.isFresh(): Boolean {
    val now = Instant.now()
    val issued = this.jwtClaimsSet.issueTime?.toInstant() ?: return false
    val expiration = this.jwtClaimsSet.expirationTime?.toInstant() ?: return false

    return !now.isBefore(issued) && now.isBefore(expiration)
}