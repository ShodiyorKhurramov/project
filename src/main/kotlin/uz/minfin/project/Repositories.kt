package minfin.uz.project

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.Optional
import javax.persistence.EntityManager

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>

class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>,
    entityManager: EntityManager,
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

}

interface FileRepository:BaseRepository<File>{
    fun findByHashIdAndDeletedFalse(hashId: String):Optional<File>
}
