package uz.minfin.project
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
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false
)



@Entity(name = "users")
class User(
    @Column(length = 30, nullable = false) var firstName: String,
    @Column(length = 30, nullable = false) var lastName: String,
    @Column(length = 16,nullable = false) var phoneNumber: String,
    @Column(length = 24,unique = true) var userName: String?,
    @Column(length = 64,nullable = false) var password: String,
    @Column(length = 14,nullable = false, unique = true) var pnfl: String,
    @Column(length=15,nullable = false)
    @Enumerated(EnumType.STRING) var role: Role = Role.USER

) : BaseEntity()


@Entity
class Project(
    @Column( nullable = false) var name: String,
    var description: String,
    @Column( nullable = false, length = 15)
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    @Column( nullable = false)  var startDate: Date,
    @Column( nullable = false) var endDate: Date,
    @OneToOne
    var logo: File?=null,
    @Enumerated(EnumType.STRING) var type: ProjectType?=null

) : BaseEntity()


@Entity
class Catalog(
    @ManyToOne
    var catalogTemplate: CatalogTemplate,
    var description: String,
    @Column( nullable = false, length = 15)
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    @Column( nullable = false)var startDate: Date,
    @Column( nullable = false) var endDate: Date,
    @ManyToOne
    var project: Project
    ) : BaseEntity()


@Entity
class CatalogTemplate(
    @Column( nullable = false) var name: String,
    var description: String,
    @OneToOne
    var logo: File?=null
) : BaseEntity()


@Entity
class Task(
    @Column( nullable = false) var name: String,
    var description: String,
    @Column( nullable = false, length = 15)
    @Enumerated(EnumType.STRING) var status: ProjectStatus = ProjectStatus.TODO,
    @Column( nullable = false) var startDate: Date,
    @Column( nullable = false)  var endDate: Date,
    @ManyToOne
    var catalog: Catalog

    ) : BaseEntity()

@Entity
class File(
    var name: String,
    var description: String,
    var hashId: String? = null,
    var mimeType: String,
    var path: String,
    @ManyToOne
    var task: Task? = null,
    var size: Long

) : BaseEntity()
