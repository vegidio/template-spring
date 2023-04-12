package io.vinicius.tplspring.config

import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWK
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.core.io.ClassPathResource

@Configuration
@EnableConfigurationProperties(CertProperties::class)
class PropertyConfiguration {
    @Bean
    fun certPropertiesConverter() = CertPropertiesConverter()
}

@ConfigurationProperties("cert")
@ConstructorBinding
data class CertProperties(
    val accessTokenPrivate: ECKey,
    val accessTokenPublic: ECKey,
    val refreshTokenPrivate: ECKey,
    val refreshTokenPublic: ECKey
)

@ConfigurationPropertiesBinding
class CertPropertiesConverter : Converter<String, ECKey> {
    override fun convert(source: String): ECKey {
        val content = ClassPathResource(source).file.readText()
        val jwk = JWK.parseFromPEMEncodedObjects(content)
        return jwk.toECKey()
    }
}