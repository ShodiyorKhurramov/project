package uz.minfin.project


enum class ErrorType(val code: Int) {
    BASE_EXCEPTION(100),
    OBJECT_NOT_FOUND(402),
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
}
