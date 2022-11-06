package uz.minfin.project

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler(private val messageSource: ResourceBundleMessageSource) : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [ProjectException::class])
    fun handleChoyException(e: ProjectException)=e.toBaseMessage(messageSource)
}