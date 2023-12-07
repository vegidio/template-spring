package io.vinicius.tplspring.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        // TODO revisit this later; hopefully there will be a solution here:
        // https://github.com/spring-projects/spring-framework/issues/31366
        @Suppress("DEPRECATION")
        configurer.setUseTrailingSlashMatch(true)
    }
}