package minfin.uz.project
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity(
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
    @Column(length=14, unique = true) var pnfl:String,
    @Enumerated(EnumType.STRING) var role: Role = Role.USER

) : BaseEntity()


@Entity
class Project(
    @Column(length = 30) var name: String,
    @Column(length = 30) var description: String?,
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    var startDate: String,
    var endDate: String,
    @OneToOne
    var logo: File,
    var type: String

) : BaseEntity()


@Entity
class Catalog(
    @ManyToOne
    var catalogTemplate: CatalogTemplate,
    @Column(length = 30) var description: String?,
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    var startDate: String,
    var endDate: String,
    @ManyToOne
    var project: Project


) : BaseEntity()


@Entity
class CatalogTemplate(
    var name: String,
    @Column(length = 30) var description: String?,
    @OneToOne
    var logo: File


) : BaseEntity()


@Entity
class Task(
    var name: String,
    @Column(length = 30) var description: String?,
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    var startDate: String,
    var endDate: String,
    @ManyToOne
    var catalog: Catalog

    ) : BaseEntity()

@Entity
class File(
    var name:String,
    @Column(length = 30) var description: String?,
    var hashId:String,
    var mimeType:String,
    var path:String,
    @ManyToMany
    var task:MutableList<Task>,
    var size:String

):BaseEntity()