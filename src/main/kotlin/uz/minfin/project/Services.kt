package minfin.uz.project

import org.springframework.context.annotation.Lazy
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageSourceService(@Lazy val messageResourceBundleMessageSource: ResourceBundleMessageSource) {

    fun getMessage(sourceKey: LocalizationTextKey): String {
        return messageResourceBundleMessageSource.getMessage(
            sourceKey.name,
            null,
            LocaleContextHolder.getLocale()
        )
    }

    fun getMessage(sourceKey: LocalizationTextKey, locale: Locale): String {
        return messageResourceBundleMessageSource.getMessage(
            sourceKey.name,
            null,
            locale
        )
    }

    fun getMessage(sourceKey: LocalizationTextKey, vararg any: String): String {
        return messageResourceBundleMessageSource.getMessage(
            sourceKey.name,
            any,
            LocaleContextHolder.getLocale()
        )
    }


}