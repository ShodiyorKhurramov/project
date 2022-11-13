package uz.minfin.project


enum class ErrorType(val code: Int) {
    BASE_EXCEPTION(100),
    OBJECT_NOT_FOUND(404),
    PROJECT_NOT_FOUND(404),
    CATALOG_NOT_FOUND(404),
    CATALOG_TEMPLATE_NOT_FOUND(404),
    TASK_NOT_FOUND(404),
    FILE_NOT_FOUND(404),
    ALREADY_REPORTED(208)
}



enum class Role{
    USER
}

enum class ProjectStatus{
    TODO,
    DOING,
    DONE,
}

enum class ProjectType{
    SIMPLE
}

enum class ErrorCode(val code: Int){
    VALIDATION_ERROR(103)
}
