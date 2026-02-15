package io.vinicius.tplspring.advice

import io.vinicius.tplspring.model.Response
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * Automatically wraps all REST responses in Response wrapper.
 *
 * This advice intercepts all controller responses and:
 * - Wraps successful responses in 'Response(data = ...)'
 * - Wraps error responses (ProblemDetail) in 'Response(error = ...)'
 * - Leaves already-wrapped Response objects unchanged
 *
 * Benefits:
 * - Controllers can return domain objects directly
 * - Consistent {"data": {...}} wrapper for successful responses
 * - Consistent {"error": {...}} wrapper for error responses
 * - No manual wrapping needed in controller methods
 */
@RestControllerAdvice
class ResponseWrapperAdvice : ResponseBodyAdvice<Any> {

    /**
     * Determines if this advice should be applied.
     * Applies to all responses except those already wrapped.
     */
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        val parameterType = returnType.parameterType
        // Don't wrap if it's already a Response
        return parameterType != Response::class.java
    }

    /**
     * Wraps the response body in Response before serialization.
     * Uses 'data' field for successful responses and the 'error' field for ProblemDetail.
     */
    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        // Don't wrap SpringDoc/OpenAPI endpoints (swagger-ui, api-docs)
        val path = request.uri.path
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) return body

        // Wrap ProblemDetail in the 'error' field
        if (body is ProblemDetail) {
            return Response<Any>(error = body)
        }

        // Wrap everything else in the 'data' field
        return Response(data = body)
    }
}