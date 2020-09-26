package pl.tysia.innovate_app.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = " https://todo.penhr.pl/"

object TodoApiClient {
    private var retrofitInstance: Retrofit? = null

    val instance: Retrofit?
        get() {

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                .create()

            if (retrofitInstance == null) retrofitInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofitInstance
        }
}