package io.vinicius.tplspring.config

import com.nimbusds.jose.jwk.JWK
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.core.io.ClassPathResource
import java.io.InputStreamReader

@Configuration
@EnableConfigurationProperties(CertProperties::class)
class PropertyConfiguration {
    @Bean
    fun certPropertiesConverter() = CertPropertiesConverter()
}

@ConfigurationProperties("cert")
data class CertProperties(
    val accessTokenPrivate: JWK,
    val accessTokenPublic: JWK,
    val refreshTokenPrivate: JWK,
    val refreshTokenPublic: JWK,
)

@ConfigurationPropertiesBinding
class CertPropertiesConverter : Converter<String, JWK> {
    override fun convert(source: String): JWK {
        val resource = ClassPathResource(source)
        val content = InputStreamReader(resource.inputStream).readText()
        val jwk = JWK.parseFromPEMEncodedObjects(content)

        // ECKey extends JWK, so we can return it directly
        return jwk.toECKey()
    }
}