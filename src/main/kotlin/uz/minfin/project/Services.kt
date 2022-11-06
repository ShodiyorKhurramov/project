package uz.minfin.project


import org.springframework.stereotype.Service


interface ProjectService{
    fun create(dto: ProjectCreateDto): ProjectResponseDto
    fun update(id: Long, dto: ProjectUpdateDto): ProjectResponseDto
    fun delete(id: Long): ProjectResponseDto
    fun getOne(id: Long): ProjectResponseDto
    fun getAll(): List<ProjectResponseDto>
}

interface CatalogService{
    fun create(dto: CatalogCreateDto): CatalogResponseDto
    fun update(id: Long, dto: CatalogUpdateDto): CatalogResponseDto
    fun delete(id: Long): CatalogResponseDto
    fun getOne(id: Long): CatalogResponseDto
    fun getAll(): List<CatalogResponseDto>
}

interface CatalogTemplateService{
    fun create(dto: CatalogTemplateCreateDto): CatalogTemplateResponseDto
    fun update(id: Long, dto: CatalogTemplateUpdateDto): CatalogTemplateResponseDto
    fun delete(id: Long): CatalogTemplateResponseDto
    fun getOne(id: Long): CatalogTemplateResponseDto
    fun getAll(): List<CatalogTemplateResponseDto>
}

interface TaskService{
    fun create(dto: TaskCreateDto): TaskResponseDto
    fun update(id: Long, dto: TaskUpdateDto): TaskResponseDto
    fun delete(id: Long): TaskResponseDto
    fun getOne(id: Long): TaskResponseDto
    fun getAll(): List<TaskResponseDto>
}



@Service class ProjectServiceImpl(private val projectRepository:ProjectRepository):ProjectService{
    override fun create(dto: ProjectCreateDto): ProjectResponseDto {
        projectRepository.existsByName(dto.name).throwIfFalse {  AlreadyReportedException() }
        return dto.run { ProjectResponseDto.toDto(projectRepository.save(Project(name,description,status,startDate,endDate,logo,type))) }
    }

    override fun update(id: Long, dto: ProjectUpdateDto): ProjectResponseDto {
        val project = projectRepository.findById(id)
        (project.isPresent && !project.get().deleted).throwIfFalse { ObjectNotFoundException() }
           return project.get().run{
                dto.name?.let { name = it }
                dto.description?.let { description = it }
                dto.status?.let { status = it }
                dto.startDate?.let { startDate = it }
                dto.endDate?.let { endDate = it }
                dto.type?.let { type = it }
                ProjectResponseDto.toDto(projectRepository.save(project.get()))
            }
      }


    override fun delete(id: Long): ProjectResponseDto {
        val project = projectRepository.findById(id)
        (project.isPresent && !project.get().deleted).throwIfFalse {  ObjectNotFoundException() }
            project.get().deleted = true
        return ProjectResponseDto.toDto(projectRepository.save(project.get()))
    }

    override fun getOne(id: Long): ProjectResponseDto {
        val project = projectRepository.findById(id)
        (project.isPresent && !project.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        return  ProjectResponseDto.toDto(project.get())

    }

    override fun getAll(): List<ProjectResponseDto> {
        val projects = projectRepository.getAllByDeletedFalse()
        projects.isEmpty().throwIfFalse {  ObjectNotFoundException() }
        return  projects.map{ProjectResponseDto.toDto(it) }

    }


}

@Service class CatalogServiceImpl(
    private val catalogRepository:CatalogRepository,
    private val catalogTemplateRepository: CatalogTemplateRepository,
    private val projectRepository: ProjectRepository
):CatalogService{
    override fun create(dto: CatalogCreateDto): CatalogResponseDto {

        catalogRepository.existsByCatalogTemplateIdAndProjectId(dto.catalogTemplateId,dto.projectId).throwIfFalse {  AlreadyReportedException() }
        val catalogTemplate=catalogTemplateRepository.findById(dto.catalogTemplateId)
        val project=projectRepository.findById(dto.projectId)
        return dto.run { CatalogResponseDto.toDto(catalogRepository.save(Catalog(catalogTemplate.get(),description,status,startDate,endDate,project.get()))) }
    }

    override fun update(id: Long, dto: CatalogUpdateDto): CatalogResponseDto {
        val catalog = catalogRepository.findById(id)
        (catalog.isPresent && !catalog.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        return catalog.get().run{
            dto.projectId?.let {
                val p= projectRepository.findById(it)
                (p.isPresent&&!p.get().deleted).throwIfFalse { ObjectNotFoundException() }
                project=p.get()
            }
            dto.description?.let { description = it }
            dto.status?.let {status = it }
            dto.startDate?.let { startDate = it }
            dto.endDate?.let { endDate= it }
            CatalogResponseDto.toDto(catalogRepository.save(catalog.get()))
        }
    }


    override fun delete(id: Long): CatalogResponseDto {
        val catalog = catalogRepository.findById(id)
        (catalog.isPresent && !catalog.get().deleted).throwIfFalse {  ObjectNotFoundException()}
        catalog.get().deleted = true
        return CatalogResponseDto.toDto(catalogRepository.save(catalog.get()))
    }

    override fun getOne(id: Long): CatalogResponseDto {
        val catalog = catalogRepository.findById(id)
        (catalog.isPresent && !catalog.get().deleted).throwIfFalse {  ObjectNotFoundException()}
        return  CatalogResponseDto.toDto(catalog.get())

    }

    override fun getAll(): List<CatalogResponseDto> {
        val catalogs = catalogRepository.getAllByDeletedFalse()
        catalogs.isEmpty().throwIfFalse {  ObjectNotFoundException() }
        return  catalogs.map{CatalogResponseDto.toDto(it) }

    }



}

@Service class CatalogTemplateServiceImpl(private val catalogTemplateRepository:CatalogTemplateRepository):CatalogTemplateService{
    override fun create(dto: CatalogTemplateCreateDto): CatalogTemplateResponseDto {
        catalogTemplateRepository.existsByName(dto.name).throwIfFalse { AlreadyReportedException() }
        return dto.run { CatalogTemplateResponseDto.toDto(catalogTemplateRepository.save(CatalogTemplate(name,description,logo))) }
    }

    override fun update(id: Long, dto: CatalogTemplateUpdateDto): CatalogTemplateResponseDto {
        val catalogTemplate = catalogTemplateRepository.findById(id)
        (catalogTemplate.isPresent && !catalogTemplate.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        return catalogTemplate.get().run{
            dto.name?.let { name = it }
            dto.description?.let { description = it }
            CatalogTemplateResponseDto.toDto(catalogTemplateRepository.save(catalogTemplate.get()))
        }
    }


    override fun delete(id: Long): CatalogTemplateResponseDto {
        val catalogTemplate = catalogTemplateRepository.findById(id)
        (catalogTemplate.isPresent && !catalogTemplate.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        catalogTemplate.get().deleted = true
        return CatalogTemplateResponseDto.toDto(catalogTemplateRepository.save(catalogTemplate.get()))
    }

    override fun getOne(id: Long): CatalogTemplateResponseDto {
        val catalogTemplate = catalogTemplateRepository.findById(id)
        (catalogTemplate.isPresent && !catalogTemplate.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        return  CatalogTemplateResponseDto.toDto(catalogTemplate.get())

    }

    override fun getAll(): List<CatalogTemplateResponseDto> {
        val catalogTemplates = catalogTemplateRepository.getAllByDeletedFalse()
        catalogTemplates.isEmpty().throwIfFalse {  ObjectNotFoundException() }
        return  catalogTemplates.map{CatalogTemplateResponseDto.toDto(it) }

    }


}

@Service class TaskServiceImpl(
    private val taskRepository:TaskRepository,
    private val catalogRepository: CatalogRepository
):TaskService{
    override fun create(dto: TaskCreateDto): TaskResponseDto {

        taskRepository.existsByNameAndCatalogId(dto.name,dto.catalogId).throwIfFalse {   AlreadyReportedException() }
        val catalog=catalogRepository.findById(dto.catalogId)
        return dto.run { TaskResponseDto.toDto(taskRepository.save(Task(name,description,status,startDate,endDate,catalog.get()))) }
    }

    override fun update(id: Long, dto: TaskUpdateDto): TaskResponseDto {
        val task = taskRepository.findById(id)
        (task.isPresent && !task.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        return task.get().run{
            dto.name?.let { name = it }
            dto.description?.let { description = it }
            dto.status?.let {status = it }
            dto.startDate?.let { startDate = it }
            dto.endDate?.let { endDate= it }
            dto.catalogId?.let {
                val c=catalogRepository.findById(it)
                (c.isPresent&& !c.get().deleted).throwIfFalse { ObjectNotFoundException() }
                catalog=c.get()
            }
            TaskResponseDto.toDto(taskRepository.save(task.get()))
        }
    }


    override fun delete(id: Long): TaskResponseDto {
        val task = taskRepository.findById(id)
        (task.isPresent && !task.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        task.get().deleted = true
        return TaskResponseDto.toDto(taskRepository.save(task.get()))
    }

    override fun getOne(id: Long): TaskResponseDto {
        val task = taskRepository.findById(id)
        (task.isPresent && !task.get().deleted).throwIfFalse {  ObjectNotFoundException() }
        return  TaskResponseDto.toDto(task.get())

    }

    override fun getAll(): List<TaskResponseDto> {
        val tasks = taskRepository.getAllByDeletedFalse()
        tasks.isEmpty().throwIfFalse {  ObjectNotFoundException() }
        return  tasks.map{TaskResponseDto.toDto(it) }

    }



}