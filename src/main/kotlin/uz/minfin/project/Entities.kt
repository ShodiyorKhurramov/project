package minfin.uz.project

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)


@Entity(name = "users")
class User(
    @Column(length = 30) var firstName: String,
    @Column(length = 30) var lastName: String?,
    @Column(length = 16) var phoneNumber: String,
    @Column(length = 24, unique = true) var userName: String,
    @Column(length = 64) var password: String,
    @Column(length = 14, unique = true) var pnfl: String,
    @Enumerated(EnumType.STRING) var role: Role = Role.USER

) : BaseEntity()


@Entity
class Project(
    var name: String,
    var description: String,
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    var startDate: Date,
    var endDate: Date,
    @OneToOne
    var logo: File,
    @Enumerated(EnumType.STRING) var type: ProjectType

) : BaseEntity()


@Entity
class Catalog(
    @ManyToOne
    var catalogTemplate: CatalogTemplate,
    var description: String,
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    var startDate: Date,
    var endDate: Date,
    @ManyToOne
    var project: Project


) : BaseEntity()


@Entity
class CatalogTemplate(
    var name: String,
    var description: String,
    @OneToOne
    var logo: File


) : BaseEntity()


@Entity
class Task(
    var name: String,
    var description: String,
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    var startDate: Date,
    var endDate: Date,
    @ManyToOne
    var catalog: Catalog

) : BaseEntity()

@Entity
class File(
    var name: String,
    var description: String,
    var hashId: String,
    var mimeType: String,
    var path: String,
    var contentType:String,
    @ManyToOne
    var task: Task? = null,
    var size: String

) : BaseEntity()