package io.vinicius.tplspring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.time.Duration

/**
 * Modern RestClient configuration for Spring Boot 4.
 * RestClient is the modern, fluent alternative to RestTemplate introduced in Spring 6.1.
 *
 * Benefits of RestClient:
 * - Fluent API similar to WebClient but for synchronous calls
 * - Built on top of the newer HttpClient API
 * - Better type safety and Kotlin support
 * - Works seamlessly with Virtual Threads
 */
@Configuration
class RestClientConfig {
    /**
     * Default RestClient builder with common configurations.
     * Use RestClient.Builder in your services to create customized instances.
     */
    @Bean
    fun restClientBuilder(): RestClient.Builder =
        RestClient
            .builder()
            .requestFactory(jdkClientHttpRequestFactory())
            .defaultHeader("User-Agent", "Template-Spring/1.0")

    /**
     * Use JDK HttpClient as the underlying HTTP client.
     * This works excellently with Virtual Threads and provides better performance.
     */
    @Bean
    fun jdkClientHttpRequestFactory(): JdkClientHttpRequestFactory {
        val requestFactory = JdkClientHttpRequestFactory()
        requestFactory.setReadTimeout(Duration.ofSeconds(10))
        return requestFactory
    }

    /**
     * Example of a configured RestClient for a specific external API.
     * In real applications, create similar beans for each external service you call.
     */
    @Bean
    fun exampleApiClient(restClientBuilder: RestClient.Builder): RestClient =
        restClientBuilder
            .baseUrl("https://api.example.com")
            .defaultHeader("Accept", "application/json")
            .build()
}