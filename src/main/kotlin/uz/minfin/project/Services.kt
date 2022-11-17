package uz.minfin.project

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.InputStreamEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.hashids.Hashids
import org.springframework.core.io.FileUrlResource
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import java.io.ByteArrayInputStream
import java.nio.file.Files
import kotlin.io.path.Path


interface ProjectService{
    fun create(dto: ProjectCreateDto): BaseMessage
    fun update(id: Long, dto: ProjectUpdateDto): ProjectResponseDto
    fun delete(id: Long): BaseMessage
    fun getOne(id: Long): ProjectResponseDto
    fun getAll(): List<ProjectResponseDto>
    fun getByStatusProjects(status: ProjectStatus): List<ProjectResponseDto>
    fun searchProject(s:String,page: Pageable,sort: String): List<ProjectResponseDto>


}


interface CatalogService{
    fun create(dto: CatalogCreateDto): BaseMessage
    fun update(id: Long, dto: CatalogUpdateDto): CatalogResponseDto
    fun delete(id: Long): BaseMessage
    fun getOne(id: Long): CatalogResponseDto
    fun getByProjectId(id:Long):List<CatalogResponseDto>
    fun getAll(): List<CatalogResponseDto>
}

interface CatalogTemplateService{
    fun create(dto: CatalogTemplateCreateDto): BaseMessage
    fun update(id: Long, dto: CatalogTemplateUpdateDto): CatalogTemplateResponseDto
    fun delete(id: Long): BaseMessage
    fun getOne(id: Long): CatalogTemplateResponseDto
    fun getAll(): List<CatalogTemplateResponseDto>
}

interface TaskService{
    fun create(dto: TaskCreateDto): BaseMessage
    fun update(id: Long, dto: TaskUpdateDto): TaskResponseDto
    fun delete(id: Long): BaseMessage
    fun getOne(id: Long): TaskResponseDto
    fun getAll(): List<TaskResponseDto>
}

@Service
interface FileService {
    fun fileUpload(dto: FileUploadDto): String?
    fun getFile(hashId: String): FileDownloadDto
    fun get(hashId: String):FileUrlResource
    fun getTaskId(id:Long):List<FileDownloadDto>
}


@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val fileRepository: FileRepository
) : ProjectService {
    override fun create(dto: ProjectCreateDto): BaseMessage{
        projectRepository.existsByName(dto.name).throwIfTrue { AlreadyReportedException() }
        return if(dto.logoHashId==null){
            dto.run {
            projectRepository.save(Project(name, description, status, startDate, endDate, null,type))
                BaseMessage.OK
            }
        }else{
            dto.run {
                val logo=fileRepository.findByHashIdAndDeletedFalse(logoHashId!!)
                (logo.isPresent).throwIfFalse { ObjectNotFoundException() }
               projectRepository.save(Project(name, description, status, startDate, endDate,logo.get(),type))
                BaseMessage.OK
            }
        }

    }
    override fun update(id: Long, dto: ProjectUpdateDto): ProjectResponseDto {
        val project = projectRepository.findByIdAndDeletedFalse(id)
        project.isPresent.throwIfFalse {ProjectNotFoundException() }
        return project.get().run {
            dto.name?.let { name = it }
            dto.description?.let { description = it }
            dto.status?.let { status = it }
            dto.startDate?.let { startDate = it }
            dto.endDate?.let { endDate = it }
            dto.type?.let { type = it }
            dto.logoHashId?.let {
                val file=fileRepository.findByHashIdAndDeletedFalse(it)
                (file.isPresent).throwIfFalse { FileNotFoundException() }
                logo=file.get()
                }
            ProjectResponseDto.toDto(projectRepository.save(project.get()))
        }
    }
    override fun delete(id: Long): BaseMessage {
        val project = projectRepository.findByIdAndDeletedFalse(id)
        project.isPresent.throwIfFalse { ProjectNotFoundException() }
        project.get().deleted = true
        projectRepository.save(project.get())
        return BaseMessage.DELETE
    }
    override fun getOne(id: Long): ProjectResponseDto {
        val project = projectRepository.findByIdAndDeletedFalse(id)
        project.isPresent.throwIfFalse { ProjectNotFoundException() }
        return ProjectResponseDto.toDto(project.get())

    }


    override fun getAll()= projectRepository.getAllByDeletedFalse().map { ProjectResponseDto.toDto(it) }
    override fun getByStatusProjects(status: ProjectStatus)=projectRepository.getAllByDeletedFalseAndStatus(status).map { ProjectResponseDto.toDto(it) }

    override fun searchProject(s: String,page: Pageable,sort:String): List<ProjectResponseDto> {

       return when(sort){
             "name"->{  projectRepository.searchName(s, page).map { ProjectResponseDto.toDto(it) } }
              "id"->{ projectRepository.searchId(s.toLong(), page).map { ProjectResponseDto.toDto(it) }}
           else -> { throw  ObjectNotFoundException()}
       }




    }

}
@Service
class CatalogServiceImpl(
    private val catalogRepository: CatalogRepository,
    private val catalogTemplateRepository: CatalogTemplateRepository,
    private val projectRepository: ProjectRepository
) : CatalogService {
    override fun create(dto: CatalogCreateDto): BaseMessage {
        catalogRepository.existsByCatalogTemplateIdAndProjectId(dto.catalogTemplateId, dto.projectId).throwIfTrue { AlreadyReportedException() }
        val catalogTemplate = catalogTemplateRepository.findByIdAndDeletedFalse(dto.catalogTemplateId)
        val project = projectRepository.findByIdAndDeletedFalse(dto.projectId)
        catalogTemplate.isPresent.throwIfFalse { CatalogTemplateNotFoundException() }
        project.isPresent.throwIfFalse { ProjectNotFoundException() }
        return dto.run {
            catalogRepository.save(Catalog(catalogTemplate.get(), description, status, startDate, endDate, project.get()))
            BaseMessage.OK
        }

    }
    override fun update(id: Long, dto: CatalogUpdateDto): CatalogResponseDto {
        val catalog = catalogRepository.findByIdAndDeletedFalse(id)
        catalog.isPresent.throwIfFalse { CatalogNotFoundException() }
       return catalog.get().run {
            dto.projectId?.let {
                val p = projectRepository.findByIdAndDeletedFalse(it)
                p.isPresent.throwIfFalse { ProjectNotFoundException() }
                project = p.get()
            }
            dto.description?.let { description = it }
            dto.status?.let { status = it }
            dto.startDate?.let { startDate = it }
            dto.endDate?.let { endDate = it }
            dto.catalogTemplateId?.let {
                val c = catalogTemplateRepository.findByIdAndDeletedFalse(it)
                c.isPresent .throwIfFalse { CatalogTemplateNotFoundException() }
                catalogTemplate = c.get()
            }
            CatalogResponseDto.toDto(catalogRepository.save(catalog.get()))
        }

    }
    override fun delete(id: Long): BaseMessage {
        val catalog = catalogRepository.findByIdAndDeletedFalse(id)
        catalog.isPresent.throwIfFalse { CatalogTemplateNotFoundException() }
        catalog.get().deleted = true
        CatalogResponseDto.toDto(catalogRepository.save(catalog.get()))
        return BaseMessage.DELETE
    }
    override fun getOne(id: Long): CatalogResponseDto {
        val catalog = catalogRepository.findByIdAndDeletedFalse(id)
        catalog.isPresent.throwIfFalse {CatalogTemplateNotFoundException() }
        return CatalogResponseDto.toDto(catalog.get())

    }

    override fun getByProjectId(id: Long)=catalogRepository.getByProjectIdAndDeletedFalse(id).map { CatalogResponseDto.toDto(it) }

    override fun getAll() = catalogRepository.getAllByDeletedFalse().map { CatalogResponseDto.toDto(it) }




}
@Service
class CatalogTemplateServiceImpl(
            private val catalogTemplateRepository: CatalogTemplateRepository,
            private val fileRepository: FileRepository
            ) : CatalogTemplateService {
            override fun create(dto: CatalogTemplateCreateDto): BaseMessage {
                catalogTemplateRepository.existsByName(dto.name).throwIfTrue { AlreadyReportedException() }
                return if (dto.logoHashId == null) {
                    dto.run {
                        catalogTemplateRepository.save(CatalogTemplate(name, description, null))
                        BaseMessage.OK
                    }
                } else {
                    dto.run {
                        val logo = fileRepository.findByHashIdAndDeletedFalse(logoHashId!!)
                        (logo.isPresent).throwIfFalse { FileNotFoundException() }
                        catalogTemplateRepository.save(CatalogTemplate(name, description, logo.get()))
                        BaseMessage.OK

                    }
                }

            }
            override fun update(id: Long, dto: CatalogTemplateUpdateDto): CatalogTemplateResponseDto {
                val catalogTemplate = catalogTemplateRepository.findByIdAndDeletedFalse(id)
                catalogTemplate.isPresent.throwIfFalse { CatalogTemplateNotFoundException()}
                return catalogTemplate.get().run {
                    dto.name?.let { name = it }
                    dto.description?.let { description = it }
                    dto.logoHashId?.let {
                        val file=fileRepository.findByHashIdAndDeletedFalse(it)
                        (file.isPresent).throwIfFalse {FileNotFoundException() }
                        logo=file.get()
                    }
                    CatalogTemplateResponseDto.toDto(catalogTemplateRepository.save(catalogTemplate.get()))
                }
            }
            override fun delete(id: Long): BaseMessage {
                val catalogTemplate = catalogTemplateRepository.findByIdAndDeletedFalse(id)
                catalogTemplate.isPresent.throwIfFalse { CatalogTemplateNotFoundException() }
                catalogTemplate.get().deleted = true
                catalogTemplateRepository.save(catalogTemplate.get())
                return BaseMessage.DELETE
            }
            override fun getOne(id: Long): CatalogTemplateResponseDto {
                val catalogTemplate = catalogTemplateRepository.findByIdAndDeletedFalse(id)
                catalogTemplate.isPresent.throwIfFalse { CatalogTemplateNotFoundException() }
                return CatalogTemplateResponseDto.toDto(catalogTemplate.get())

            }
            override fun getAll()= catalogTemplateRepository.getAllByDeletedFalse().map { CatalogTemplateResponseDto.toDto(it) }



}
@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository, private val catalogRepository: CatalogRepository
) : TaskService {
    override fun create(dto: TaskCreateDto): BaseMessage{
        taskRepository.existsByNameAndCatalogId(dto.name, dto.catalogId).throwIfTrue { AlreadyReportedException() }
        val catalog = catalogRepository.findByIdAndDeletedFalse(dto.catalogId)
        catalog.isPresent.throwIfFalse { CatalogNotFoundException() }
        return  dto.run { taskRepository.save(Task(name, description, status, startDate, endDate, catalog.get()))
            BaseMessage.OK}

    }
    override fun update(id: Long, dto: TaskUpdateDto): TaskResponseDto {
        val task = taskRepository.findByIdAndDeletedFalse(id)
        task.isPresent.throwIfFalse { TaskNotFoundException() }
       return task.get().run {
            dto.name?.let { name = it }
            dto.description?.let { description = it }
            dto.status?.let { status = it }
            dto.startDate?.let { startDate = it }
            dto.endDate?.let { endDate = it }
            dto.catalogId?.let {
                val c = catalogRepository.findByIdAndDeletedFalse(it)
                c.isPresent.throwIfFalse { CatalogNotFoundException() }
                catalog = c.get()
            }
            TaskResponseDto.toDto(taskRepository.save(task.get()))
        }

    }
    override fun delete(id: Long): BaseMessage {
        val task = taskRepository.findByIdAndDeletedFalse(id)
        task.isPresent.throwIfFalse { TaskNotFoundException() }
        task.get().deleted = true
        TaskResponseDto.toDto(taskRepository.save(task.get()))
        return BaseMessage.DELETE
    }
    override fun getOne(id: Long): TaskResponseDto {
        val task = taskRepository.findByIdAndDeletedFalse(id)
        task.isPresent.throwIfFalse { TaskNotFoundException() }
        return TaskResponseDto.toDto(task.get())

    }
    override fun getAll() = taskRepository.getAllByDeletedFalse().map { TaskResponseDto.toDto(it) }



}


@Service
interface UserService {
    fun create()
}
@Service
class AuthService(

    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        var optionalUser = userRepository.findByUserName(username)
        if (!optionalUser.isPresent) {
            throw UsernameNotFoundException("user not found with username : $username")
        } else {
            val user = optionalUser.get()
            return User.builder().username(user.userName).password(user.password)
                .authorities(mutableListOf(SimpleGrantedAuthority("ROLE_" + user.role))).build()
        }
    }

    fun login(dto: LoginDto)/*SessionDTO*/ {
        val httpClient = HttpClientBuilder.create().build()
        val httpPost = HttpPost("/api/login")
        val bytes = ObjectMapper().writeValueAsBytes(dto)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        val httpHeaders = HttpHeaders()
        httpPost.entity = InputStreamEntity(byteArrayInputStream)
        val response = httpClient.execute(httpPost)
    }

}
class UserServiceImpl : UserService {
    override fun create() {
        TODO("Not yet implemented")
    }

}


@Service
class FileServiceImpl(
    private val fileRepository: FileRepository, private val taskRepository: TaskRepository
) : FileService {
    override fun fileUpload(dto: FileUploadDto): String? {
        var uploadFolder = "logo"
        var task: Task? = null
        dto.apply {
            taskId?.let {
                val optionalTaks = taskRepository.findById(it)
                if (optionalTaks.isPresent) {
                    task = optionalTaks.get()
                    uploadFolder = task!!.catalog.project.name + '\\' + task!!.catalog.catalogTemplate.name + '\\' + task!!.name
                }
            }
            if (!java.io.File(uploadFolder).exists()) {
                java.io.File(uploadFolder).mkdirs()
            }
            val file = fileRepository.save(
                File(
                    multipartFile.originalFilename!!,
                    description,
                    null,
                    multipartFile.contentType!!,
                    uploadFolder,
                    task,
                    multipartFile.size
                )
            )
            file.hashId = Hashids(multipartFile.name, 8).encode(file.id!!)
            fileRepository.save(file)
            Files.copy(multipartFile.inputStream, Path(uploadFolder))
            return file.hashId;
        }
    }
    override fun getFile(hashId: String): FileDownloadDto {
        val file = fileRepository.findByHashIdAndDeletedFalse(hashId)
        file.isPresent.throwIfFalse { ObjectNotFoundException() }
        return FileDownloadDto.toDto(file.get())
    }
    override fun get(hashId: String):FileUrlResource  {
        val file = fileRepository.findByHashIdAndDeletedFalse(hashId)
        file.isPresent.throwIfFalse { ObjectNotFoundException() }
        return FileUrlResource(file.get().path)
    }
    override fun getTaskId(id: Long): List<FileDownloadDto> {
        val list = fileRepository.findByTaskId(id)
        list.isEmpty().throwIfTrue { ObjectNotFoundException() }
        return list.map { FileDownloadDto.toDto(it) }
    }


}





