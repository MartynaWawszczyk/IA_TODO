package pl.tysia.innovate_app.data

import io.reactivex.Observable
import pl.tysia.innovate_app.data.model.APIResponse
import retrofit2.http.*


private const val TOKEN = "605bcd542e2fbb36f85e3d8661ea7bdf8583"

interface TodoApi {

    @Headers("Authorization: $TOKEN")
    @GET("api/todos")
    fun getTasks(@Query("title") title : String? = null,
                 @Query("completed") completed : Boolean? = null,
                 @Query("priority") priority : Boolean? = null,
                 @Query("order_by") orderBy : String? = null,
                 @Query("page") page : Int? = null,
                 @Query("per_page") perPage : Int? = null) : Observable<APIResponse.GetTasks>


    @Headers("Authorization: $TOKEN")
    @POST("api/todos")
    fun createTask(@Query("todo_id") id : String,
                   @Query("title") title : String? = null) : Observable<APIResponse.Create>


    @Headers("Authorization: $TOKEN")
    @DELETE("api/todos/{id}")
    fun deleteTask( @Path("id") id : String, @Query("todo_id") todoID : String = id) : Observable<APIResponse.Delete>


    @Headers("Authorization: $TOKEN")
    @PATCH("api/todos/{id}")
    fun updateTask(@Path("id") id: String,
                   @Query("title") title: String? = null,
                   @Query("description") description: String? = null,
                   @Query("completed") completed: Boolean? = null,
                   @Query("priority") priority: Boolean? = null,
                   @Query("deadline_at") deadline: String? = null,
                   @Query("todo_id") todoID: String = id) : Observable<APIResponse.Update>


}