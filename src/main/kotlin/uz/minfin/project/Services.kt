package minfin.uz.project

import org.hashids.Hashids
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Lazy
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartHttpServletRequest
import uz.minfin.project.FileUploadDto
import uz.minfin.project.getFileExtention
import java.lang.Math.abs
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.io.path.Path


@Service
interface FileService{
    fun fileUpload(request: MultipartHttpServletRequest,dto: FileUploadDto)
}
class FileServiceImpl(
    private val fileRepository: FileRepository
):FileService{
    override fun fileUpload(request: MultipartHttpServletRequest,dto: FileUploadDto) {
        val uploadFolder = dto.projectName +'\\'+ dto.catologName +'\\'+dto.task+'\\'
        if (!java.io.File(uploadFolder).exists()){
            java.io.File(uploadFolder).mkdirs()
        }
        fileRepository.save(request.fileNames.next().let { getFileExtention(it!!) }.let {
            File(
                request.fileNames.next(),
                dto.description,
                Hashids(request.fileNames.next(),8).encode(abs(Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).nextLong())),
                it!!,
                uploadFolder,
                request.contentType,
                dto.task,
                request.getFile(request.fileNames.next())!!.size.toString()
            )
        })
        Files.copy(request.getFile(request.fileNames.next())!!.inputStream, Path(uploadFolder))
    }

}
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