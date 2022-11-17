package uz.minfin.project
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank



data class ProjectCreateDto(

     @get:NotBlank
     var name: String,
     var description: String,
     var status: ProjectStatus=ProjectStatus.TODO,
     var startDate: Date,
     var endDate: Date,
     var logoHashId: String?=null,
     var type: ProjectType?=null
)
data class ProjectUpdateDto(
     var name: String?=null,
     var description: String?=null,
     var status: ProjectStatus?=null,
     var startDate: Date?=null,
     var endDate: Date?=null,
     var logoHashId: String?=null,
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
     var logoHashId: String?=null,
     var type: ProjectType?=null
){
     companion object{

          fun toDto(p:Project) = p.run {

               if(logo?.hashId !=null){
                    ProjectResponseDto(id!!,name,description,status,startDate,endDate, logo!!.hashId,type)
               }else{
                    ProjectResponseDto(id!!,name,description,status,startDate,endDate, null,type)

               }

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
     var projectId: Long?

) {
     companion object {
          fun toDto(c: Catalog) = c.run {
               CatalogResponseDto(id!!, CatalogTemplateResponseDto.toDto(catalogTemplate), description, status, startDate, endDate,project.id)
          }
     }
}


data class CatalogTemplateCreateDto(
     @get:NotBlank var name: String, var description: String, var logoHashId: String? = null
)
data class CatalogTemplateUpdateDto(var name: String? = null, var description: String? = null, var logoHashId: String?=null)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CatalogTemplateResponseDto(
     var id:Long,
     var name: String,
     var description: String,
     var logoHashId: String?=null
){
     companion object{
          fun toDto(c:CatalogTemplate) = c.run {

               if(logo?.hashId !=null){
                    CatalogTemplateResponseDto(id!!,name,description,logo!!.hashId)
               }else{
                    CatalogTemplateResponseDto(id!!,name,description,null)

               }

          }
     }
}


data class TaskCreateDto(
     @get:NotBlank
     var name: String,
     @get:NotBlank
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
     var catalog:CatalogResponseDto?=null,
     var catalogId:Long?=null,
){
     companion object{

          fun toDto(t:Task) = t.run {
               TaskResponseDto(id!!,name,description,status,startDate,endDate,CatalogResponseDto.toDto(catalog))
          }

          fun toDtoId(t:Task)=t.run {
               TaskResponseDto(id!!,name,description,status,startDate,endDate,null,catalog.id)
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

