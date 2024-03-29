package io.vinicius.tplspring.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Template Spring")
                    .description("API to control authentication and fetch info about countries")
                    .version("1.0")
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "access-token",
                        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                    )
                    .addSecuritySchemes(
                        "refresh-token",
                        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                    )
            )
    }

    @Bean
    fun sortSchemasAlphabetically() = OpenApiCustomiser {
        val schemas = it.components.schemas
        it.components.schemas = schemas.toSortedMap()
    }
}