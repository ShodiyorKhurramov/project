package uz.minfin.project

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    fun findByIdAndDeletedFalse(id: Long):Optional<Project>
    @Query("select p from Project p where p.deleted = false")
    fun getAllByDeletedFalse():List<Project>

    @Query("select p from Project p where p.deleted = false and p.status = ?1")
    fun getAllByDeletedFalseAndStatus(status: ProjectStatus):List<Project>

    @Query("select * from project p where p.name  ILIKE  CONCAT('%', :name, '%') and p.deleted=false ", nativeQuery = true)
    fun searchName(name:String,pageable: Pageable): Page<Project>

    @Query("select * from project p  where p.id=:id and p.deleted=false", nativeQuery = true)
    fun searchId(id:Long,pageable: Pageable):Page<Project>



}

interface CatalogRepository:BaseRepository<Catalog>{
    fun existsByCatalogTemplateIdAndProjectId(catalogTemplateId: Long, projectId: Long):Boolean
    fun findByIdAndDeletedFalse(id: Long):Optional<Catalog>
    fun getAllByDeletedFalse():List<Catalog>

    @Query("select c from Catalog c where c.project.id = ?1 and c.deleted = false")
    fun getByProjectIdAndDeletedFalse(projectId: Long):List<Catalog>
}

interface CatalogTemplateRepository:BaseRepository<CatalogTemplate>{

    fun existsByName(name: String):Boolean
    fun findByIdAndDeletedFalse(id: Long):Optional<CatalogTemplate>
    fun getAllByDeletedFalse():List<CatalogTemplate>

}

interface TaskRepository:BaseRepository<Task>{

    fun existsByNameAndCatalogId(name: String, catalogId: Long):Boolean
    fun findByIdAndDeletedFalse(id: Long):Optional<Task>
    fun getAllByDeletedFalse():List<Task>

    @Query("select t from Task t where t.deleted = false and t.catalog.id = ?1")
    fun getAllByDeletedFalseAndCatalogId(catalogId: Long):List<Task>

}

interface FileRepository:BaseRepository<File>{
    fun findByHashIdAndDeletedFalse(hashId: String):Optional<File>
    fun findByTaskId(task_id: Long):List<File>
    fun existsByHashId(hashId: String):Boolean
}
interface UserRepository: BaseRepository<User> {
    fun findByUserName(username:String):Optional<User>
    fun existsByUserName(userName: String):Boolean
    fun existsByPhoneNumber(phoneNumber: String):Boolean
}
