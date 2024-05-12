package prj.applista

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import prj.applista.adapters.ListaContatoAdapter
import prj.applista.api.ApiClient
import prj.applista.dao.DaoContatos
import prj.applista.models.Contato
import retrofit2.Callback
import prj.applista.interfaces.ContatoService
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var listContatos: RecyclerView
    lateinit var ListaContatosDados:List<Contato>
    lateinit var btCadastroContato:FloatingActionButton

    override fun onResume() {
        super.onResume()
        atualizarListaContatos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btCadastroContato = findViewById(R.id.btCadastrarContato)

        listContatos = findViewById(R.id.listContatos)

        atualizarListaContatos()

        btCadastroContato.setOnClickListener{
            val intent = Intent(this,FormContatoActivity::class.java)
            startActivity(intent)
        }

    }
    private fun atualizarListaContatos() {
        val retrofit = ApiClient.client
        val call = retrofit.create(ContatoService::class.java)

        call.getContatos().enqueue(object : Callback<List<Contato>> {
            override fun onResponse(
                call: Call<List<Contato>>,
                response: Response<List<Contato>>
            ) {
                if (response != null && response.body() != null) {
                    listContatos.adapter = ListaContatoAdapter(response.body()!!)
                    ListaContatosDados = response.body()!!
                }
            }

            override fun onFailure(call: Call<List<Contato>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Falha ao carregar os contatos.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val adapter: ListaContatoAdapter = listContatos.adapter as ListaContatoAdapter
        val posicao: Int = adapter.posicaoClicada

        val contato: Contato = ListaContatosDados.get(posicao)

        if (item.itemId == R.id.menu_mapa) {
            val uri: Uri = Uri.parse("geo:0,0?q=" + contato.endereco + "&z=18")
            val intent: Intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } else if (item.itemId == R.id.menu_ligacao) {
            val uri: Uri = Uri.parse("tel:" + contato.telefone)
            val intent: Intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)


        } else if (item.itemId == R.id.menu_email) {
            val uri: Uri = Uri.parse("mailto:" + contato.email)
            val intent: Intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } else if (item.itemId == R.id.menu_detalhes) {
            val intent: Intent = Intent(this, DetalhesContatoActivity::class.java)
            intent.putExtra("contato", contato)
            startActivity(intent)

        }else if (item.itemId == R.id.menu_excluir){
            val retrofit = ApiClient.client
            val call = retrofit.create(ContatoService::class.java)

            call.deleteContato(contato._id).enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@MainActivity, "Excluído com sucesso", Toast.LENGTH_LONG).show()
                        atualizarListaContatos()
                    }

                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Erro" + t.message, Toast.LENGTH_LONG).show()
                }

            })
        }
        else if(item.itemId== R.id.menu_editar)
        {
            var intent = Intent(this,FormContatoActivity::class.java)
            intent.putExtra("contato", contato)
            startActivity(intent)
        }


        return super.onContextItemSelected(item)

    }
}