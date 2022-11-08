package uz.minfin.project
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ProjectCreateDto(
     @field: Size(min = 3, max = 100, message = "ssss",)
     var name: String,
     @field: NotBlank
     var description: String,
     var status: ProjectStatus=ProjectStatus.TODO,
     var startDate: Date,
     var endDate: Date,
     var logo: File?=null,
     var type: ProjectType?=null
)


data class ProjectUpdateDto(
     var name: String?=null,
     var description: String?=null,
     var status: ProjectStatus?=null,
     var startDate: Date?=null,
     var endDate: Date?=null,
     var logo: File?=null,
     var type: ProjectType?=null

)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProjectResponseDto(
     var id:Long,
     var name: String,
     var description: String,
     var status: ProjectStatus = ProjectStatus.TODO,
     var startDate: Date,
     var endDate: Date,
     var logo: File?=null,
     var type: ProjectType?=null
){
     companion object{

          fun toDto(p:Project) = p.run {
               ProjectResponseDto(id!!,name,description,status,startDate,endDate)
          }
     }
}


data class CatalogCreateDto(
     var catalogTemplateId: Long,
     var description: String,
     var status: ProjectStatus=ProjectStatus.TODO,
     var startDate: Date,
     var endDate: Date,
     var projectId: Long

     )


data class CatalogUpdateDto(
     var catalogTemplateId: Long?=null,
     var description: String?=null,
     var status: ProjectStatus?=null,
     var startDate: Date?=null,
     var endDate: Date?=null,
     var projectId: Long?=null


)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CatalogResponseDto(
     var id: Long,
     var catalogTemplate: CatalogTemplateResponseDto,
     var description: String,
     var status: ProjectStatus = ProjectStatus.TODO,
     var startDate: Date,
     var endDate: Date,
     var project: ProjectResponseDto

) {
     companion object {
          fun toDto(c: Catalog) = c.run {
               CatalogResponseDto(id!!, CatalogTemplateResponseDto.toDto(catalogTemplate), description, status, startDate, endDate, ProjectResponseDto.toDto(project))
          }
     }
}


data class CatalogTemplateCreateDto(var name: String, var description: String, var logo: File? = null)
data class CatalogTemplateUpdateDto(var name: String? = null, var description: String? = null, var logo: File? = null)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CatalogTemplateResponseDto(var id:Long,var name: String, var description: String, var logo: File? = null){
     companion object{
          fun toDto(c:CatalogTemplate) = c.run {
               CatalogTemplateResponseDto(id!!,name,description,logo)
          }
     }
}



data class TaskCreateDto(
     var name: String,
     var description: String,
     var status: ProjectStatus = ProjectStatus.TODO,
     var startDate: Date,
     var endDate: Date,
     var catalogId: Long
)


data class TaskUpdateDto(
     var name: String?=null,
     var description: String?=null,
     var status: ProjectStatus?= ProjectStatus.TODO,
     var startDate: Date?=null,
     var endDate: Date?=null,
     var catalogId: Long?=null

)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TaskResponseDto(
     var id : Long,
     var name: String,
     var description: String,
     var status: ProjectStatus = ProjectStatus.TODO,
     var startDate: Date,
     var endDate: Date,
     var catalog:CatalogResponseDto
){
     companion object{

          fun toDto(t:Task) = t.run {
               TaskResponseDto(id!!,name,description,status,startDate,endDate,CatalogResponseDto.toDto(catalog))
          }
     }
}



data class FileUploadDto(
     var description: String,
     var multipartFile: MultipartFile,
     var taskId:Long?
)

data class FileDownloadDto(
    var name: String,
    var description: String,
    var hashId: String
){
     companion object {
          fun toDto(f:File) = f.run {
               FileDownloadDto(name,description,hashId!!)
          }
     }
}

data class LoginDto(
    val userName: String,
    val password: String
)
data class SessionDTO(
    val accessTokenExpiry: Date,
    val issuedAt: Long,
    val accessToken:String,
)

data class AppErrorDto(
     var timestamp: Timestamp? = Timestamp.valueOf(LocalDateTime.now()),
     var status: HttpStatus,
     var error:String? = status.reasonPhrase,
     var message:String?,
     var path:String,
)

