package io.vinicius.tplspring.domain.country.converter

import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.stereotype.Component

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class CountryCode

@Component
class CountryCodeConverter : ConditionalGenericConverter {
    override fun matches(
        sourceType: TypeDescriptor,
        targetType: TypeDescriptor,
    ): Boolean = targetType.hasAnnotation(CountryCode::class.java)

    override fun getConvertibleTypes(): Set<ConvertiblePair> =
        setOf(ConvertiblePair(String::class.java, String::class.java))

    override fun convert(
        source: Any?,
        sourceType: TypeDescriptor,
        targetType: TypeDescriptor,
    ): String = (source as String).uppercase()
}