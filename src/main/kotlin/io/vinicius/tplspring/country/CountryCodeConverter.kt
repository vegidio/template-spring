package io.vinicius.tplspring.country

import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
import org.springframework.stereotype.Component

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class CountryCode

@Component
class CountryCodeConverter : ConditionalGenericConverter {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return targetType.getAnnotation(CountryCode::class.java) != null
    }

    override fun getConvertibleTypes(): MutableSet<ConvertiblePair>? {
        return mutableSetOf(ConvertiblePair(String::class.java, String::class.java))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): String {
        return (source as String).uppercase()
    }
}