package pl.tysia.innovate_app.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Task(var id : String, var title : String) : Serializable {
    companion object{
        const val TASK_EXTRA = "pl.tysia.innovate_app.task_extra"
    }

    var description : String? = null
    var priority = false
    @SerializedName("deadline_at")
    var deadlineAt : Date? = null
    var completed = false
}