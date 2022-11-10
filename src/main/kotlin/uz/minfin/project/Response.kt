package uz.minfin.project


data class BaseMessage(val code: Int, val message: String) {


    companion object {

        val OK = BaseMessage(200, "Success")
        val DELETE=BaseMessage(200,"Delete")
    }
}



