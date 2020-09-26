package pl.tysia.innovate_app.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import pl.tysia.innovate_app.data.model.APIResponse
import pl.tysia.innovate_app.data.TodoApi
import pl.tysia.innovate_app.data.TodoApiClient
import retrofit2.Retrofit


class TodoViewModel : ViewModel() {
    private var api : TodoApi
    private var disposable : CompositeDisposable = CompositeDisposable()

    val taskResult = MutableLiveData<APIResponse>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()

    init {
        val retrofit: Retrofit = TodoApiClient.instance!!
        api = retrofit.create<TodoApi>(
            TodoApi::class.java)
    }

    fun getTasks(title : String? = null,
                 completed : Boolean? = null,
                 priority : Boolean? = null,
                 orderBy : String? = null,
                 page : Int? = null,
                 perPage : Int? = null){

        disposable.add(
            api.getTasks(title, completed, priority, orderBy, page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {loading.value = true}
                .doAfterTerminate { loading.value = false }
                .subscribe (
                    {
                            result -> taskResult.value = result

                    },
                    {
                            t -> error.value = t
                    }
                )
        )

    }

    fun createTask( id : String,
                 title : String){

        disposable.add(
            api.createTask(id, title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {loading.value = true}
                .doAfterTerminate { loading.value = false }
                .subscribe (
                    { result -> taskResult.value = result },
                    { t -> error.value = t  }
                )
        )

    }

    fun deleteTask( id : String){

        disposable.add(
            api.deleteTask(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {loading.value = true}
                .doAfterTerminate { loading.value = false }
                .subscribe (
                    { result -> taskResult.value = result },
                    { t -> error.value = t  }
                )
        )

    }

    fun updateTask(id: String,
                   title: String? = null,
                   description: String? = null,
                   completed: Boolean? = null,
                   priority: Boolean? = null,
                   deadline: String? = null) {

        disposable.add(
            api.updateTask(id, title, description, completed, priority, deadline)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {loading.value = true}
                .doAfterTerminate { loading.value = false }
                .subscribe (
                    {
                            result -> taskResult.value = result
                    },
                    {
                            t -> error.value = t
                    }
                )
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}