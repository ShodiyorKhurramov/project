package uz.minfin.project

import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/file")
class FileController(
    private val fileService: FileServiceImpl
) {
    @PostMapping("/upload"/*, consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE)*/)
    fun saveFile(@RequestBody dto: FileUploadDto) = fileService.fileUpload(dto)
}


@RestController
@RequestMapping("api/v1/project")
class ProjectController(private val projectService: ProjectService){
    @PostMapping("create")
    fun creates(@Valid @RequestBody dto: ProjectCreateDto) = projectService.create(dto)

    @PutMapping("update/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: ProjectUpdateDto) = projectService.update(id, dto)

    @DeleteMapping("delete/{id}")
    fun delete(@PathVariable id: Long) = projectService.delete(id)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = projectService.getOne(id)

    @GetMapping("getAll")
    fun getAll() = projectService.getAll()

    @GetMapping("getByStatusProject/{status}")
    fun getByStatusProject(@PathVariable status:ProjectStatus) = projectService.getByStatusProject(status)

    @GetMapping("search/{s}")
    fun searchProject(@PathVariable s: String,page:Int,size:Int,sort:String)= projectService.searchProject(s, PageRequest.of(page,size),sort)
}

@RestController
@RequestMapping("api/v1/catalog")
class CatalogController(private val catalogService: CatalogService){
    @PostMapping("create")
    fun creates(@RequestBody dto: CatalogCreateDto) = catalogService.create(dto)

    @PutMapping("update/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CatalogUpdateDto) = catalogService.update(id, dto)

    @DeleteMapping("delete/{id}")
    fun delete(@PathVariable id: Long) = catalogService.delete(id)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = catalogService.getOne(id)

    @GetMapping("getByProjectId/{id}")
    fun getByProjectId( @PathVariable id: Long) = catalogService.getByProjectId(id)

    @GetMapping("getAll")
    fun getAll() = catalogService.getAll()

}

@RestController
@RequestMapping("api/v1/catalogTemplate ")
class CatalogTemplateController(private val catalogTemplateService: CatalogTemplateService){
    @PostMapping("create")
    fun creates(@Valid @RequestBody dto: CatalogTemplateCreateDto) = catalogTemplateService.create(dto)

    @PutMapping("update/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CatalogTemplateUpdateDto) = catalogTemplateService.update(id, dto)

    @DeleteMapping("delete/{id}")
    fun delete(@PathVariable id: Long) = catalogTemplateService.delete(id)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = catalogTemplateService.getOne(id)

    @GetMapping("getAll")
    fun getAll() = catalogTemplateService.getAll()
}

@RestController
@RequestMapping("api/v1/task")
class TaskController(private val taskService: TaskService){
    @PostMapping("create")
    fun creates(@Valid @RequestBody dto: TaskCreateDto) = taskService.create(dto)

    @PutMapping("update/{id}")
    fun update(@PathVariable id: Long,@Valid @RequestBody dto: TaskUpdateDto) = taskService.update(id, dto)

    @DeleteMapping("delete/{id}")
    fun delete(@PathVariable id: Long) = taskService.delete(id)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = taskService.getOne(id)

    @GetMapping("getByCatalogId/{id}")
    fun getByCatalogId( @PathVariable id: Long) = taskService.getByCatalogId(id)

    @GetMapping("getAll")
    fun getAll() = taskService.getAll()
}

@RestController
    @RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
){
    @PostMapping("login")
    fun authLogin(@RequestBody dto: LoginDto) = authService.login(dto)

}