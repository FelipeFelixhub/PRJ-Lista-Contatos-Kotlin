package prj.applista.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://web-sxx83k0kx6ki.up-de-fra1-k8s-1.apps.run-on-seenode.com/" // Sua URL base
    private var retrofit: Retrofit? = null

    val client:Retrofit
    get(){
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!


    }
}
