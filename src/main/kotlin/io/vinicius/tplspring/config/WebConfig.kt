package io.vinicius.tplspring.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    // Note: setUseTrailingSlashMatch has been removed in Spring Boot 4.0
    // Trailing slash matching is now disabled by default
}