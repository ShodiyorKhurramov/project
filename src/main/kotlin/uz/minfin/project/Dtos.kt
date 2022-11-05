package uz.minfin.project

import minfin.uz.project.Task
import javax.persistence.ManyToOne


data class FileUploadDto(
    var description: String,
    var projectName:String,
    var catologName: String,
    var task: Task
)