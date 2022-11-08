package uz.minfin.project
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*
import javax.validation.constraints.NotNull


data class ProjectCreateDto(
     var name: String,
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
    var projectName:String,
    var catologName: String,
    var task: Task
)

