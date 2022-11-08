package uz.minfin.project

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.function.Consumer


@ControllerAdvice
class GlobalExceptionHandler(private val messageSource: ResourceBundleMessageSource) : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [ProjectException::class])
    fun handleChoyException(e: ProjectException)=e.toBaseMessage(messageSource)

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            val message = error.getDefaultMessage()
            val code = error.code
            val codeError = "code"
            errors[fieldName] = message
            errors[codeError] = error.code
        })

        return ResponseEntity((errors), HttpStatus.BAD_REQUEST)
    }
}

