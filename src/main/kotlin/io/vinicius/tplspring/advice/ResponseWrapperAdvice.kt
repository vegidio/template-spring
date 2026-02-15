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
 * Automatically wraps all successful REST responses in Response<T> wrapper.
 *
 * This advice intercepts all controller responses and:
 * - Wraps domain objects in Response(data = ...) for consistency
 * - Leaves ProblemDetail responses unwrapped (handled by exception handler)
 * - Leaves already-wrapped Response<T> objects unchanged
 *
 * Benefits:
 * - Controllers can return domain objects directly
 * - Consistent {"data": {...}} wrapper for all successful responses
 * - No manual wrapping needed in controller methods
 */
@RestControllerAdvice
class ResponseWrapperAdvice : ResponseBodyAdvice<Any> {
    /**
     * Determines if this advice should be applied.
     * Only applies to non-ProblemDetail and non-Response objects.
     */
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        val parameterType = returnType.parameterType

        // Don't wrap if it's already a Response or ProblemDetail
        return parameterType != Response::class.java &&
            parameterType != ProblemDetail::class.java &&
            !ProblemDetail::class.java.isAssignableFrom(parameterType)
    }

    /**
     * Wraps the response body in Response<T> before serialization.
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

        // If the body is null, return Response with null data
        // Otherwise wrap the body in Response
        return Response(data = body)
    }
}