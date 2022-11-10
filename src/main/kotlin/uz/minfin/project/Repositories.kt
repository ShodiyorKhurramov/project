package uz.minfin.project

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*
import javax.persistence.EntityManager

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>

class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>,
    entityManager: EntityManager,
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

}

interface ProjectRepository:BaseRepository<Project>{

    fun existsByName(name: String):Boolean
    fun getAllByDeletedFalse():List<Project>

}

interface CatalogRepository:BaseRepository<Catalog>{
    fun existsByCatalogTemplateIdAndProjectId(catalogTemplateId: Long, projectId: Long):Boolean
    fun getAllByDeletedFalse():List<Catalog>
}

interface CatalogTemplateRepository:BaseRepository<CatalogTemplate>{

    fun existsByName(name: String):Boolean
    fun getAllByDeletedFalse():List<CatalogTemplate>

}

interface TaskRepository:BaseRepository<Task>{

    fun existsByNameAndCatalogId(name: String, catalogId: Long):Boolean

    fun getAllByDeletedFalse():List<Task>

}

interface FileRepository:BaseRepository<File>{
    fun findByHashIdAndDeletedFalse(hashId: String):Optional<File>
    fun findByTaskId(task_id: Long):List<File>
    fun existsByHashId(hashId: String):Boolean
}
interface UserRepository: BaseRepository<User> {
    fun findByUserName(username:String):Optional<User>
}
