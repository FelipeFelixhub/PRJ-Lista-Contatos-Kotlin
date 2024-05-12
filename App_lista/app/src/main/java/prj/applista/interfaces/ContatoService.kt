package prj.applista.interfaces

import prj.applista.models.Contato
import retrofit2.Call
import retrofit2.http.*

interface ContatoService {

    @GET("contatos")
    fun getContatos():Call<List<Contato>>

    @GET("contatos/{id}")
    fun getContato(@Path("id") id: String): Call<Contato>

    @POST("contatos")
    fun createContato(@Body contato: Contato): Call<Contato>

    @PUT("contatos/{id}")
    fun updateContato(@Path("id") id: String, @Body contato: Contato): Call<Contato>

    @DELETE("contatos/{id}")
    fun deleteContato(@Path("id") id: String): Call<Void>

}