package io.vinicius.tplspring.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(TrailingSlashRedirectInterceptor())
    }

    /**
     * Redirects URLs with trailing slashes to the same URL without the trailing slash.
     */
    class TrailingSlashRedirectInterceptor : HandlerInterceptor {
        override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
            val uri = request.requestURI

            if (uri.length > 1 && uri.endsWith("/")) {
                val urlWithoutSlash = uri.dropLast(1)
                val redirectUrl = request.queryString?.let { "$urlWithoutSlash?$it" } ?: urlWithoutSlash
                response.sendRedirect(redirectUrl)
                return false
            }

            return true
        }
    }
}