package io.vinicius.tplspring.config

import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWK
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Configuration
@ConfigurationProperties("cert")
data class CertProperties(
    var accessTokenPrivate: ECKey? = null,
    var accessTokenPublic: ECKey? = null,
    var refreshTokenPrivate: ECKey? = null,
    var refreshTokenPublic: ECKey? = null
)

@Component
@ConfigurationPropertiesBinding
private class CertPropertiesConverter : Converter<String, ECKey> {
    override fun convert(source: String): ECKey {
        val content = ClassPathResource(source).file.readText()
        val jwk = JWK.parseFromPEMEncodedObjects(content)
        return jwk.toECKey()
    }
}