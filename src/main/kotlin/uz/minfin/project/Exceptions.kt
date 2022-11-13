package uz.minfin.project

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity


abstract class ProjectException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorType

    fun toBaseMessage(messageSource: ResourceBundleMessageSource): ResponseEntity<BaseMessage> {
        return ResponseEntity.badRequest().body(
            BaseMessage(
                errorType().code, messageSource.getMessage(
                    errorType().name, null, LocaleContextHolder.getLocale()
                )
            )
        )
    }
}

class BaseException() : ProjectException() {
    override fun errorType() = ErrorType.BASE_EXCEPTION
}

class ObjectNotFoundException() : ProjectException() {
    override fun errorType() = ErrorType.OBJECT_NOT_FOUND

}

class ProjectNotFoundException() : ProjectException() {
    override fun errorType() = ErrorType.PROJECT_NOT_FOUND

}

class CatalogNotFoundException() : ProjectException() {
    override fun errorType() = ErrorType.CATALOG_NOT_FOUND

}

class CatalogTemplateNotFoundException() : ProjectException() {
    override fun errorType() = ErrorType.CATALOG_TEMPLATE_NOT_FOUND

}

class TaskNotFoundException() : ProjectException() {
    override fun errorType() = ErrorType.TASK_NOT_FOUND

}

class FileNotFoundException() : ProjectException() {
    override fun errorType() = ErrorType.FILE_NOT_FOUND

}

class AlreadyReportedException() : ProjectException() {
    override fun errorType() = ErrorType.ALREADY_REPORTED
}




data class ValidationErrorMessage(val code: Int, val message: String, val fields: Map<String, Any?>)


