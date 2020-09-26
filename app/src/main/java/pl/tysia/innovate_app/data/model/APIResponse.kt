package pl.tysia.innovate_app.data.model


sealed class APIResponse() {

    data class Delete(val id : String) : APIResponse()
    data class Create(val id : String) : APIResponse()
    data class Update(val id : String) : APIResponse()
    data class GetTasks(var results : List<Task>, var count : Int) : APIResponse()

}