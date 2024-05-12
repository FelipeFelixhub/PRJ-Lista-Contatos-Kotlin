package prj.applista

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import prj.applista.api.ApiClient
import prj.applista.interfaces.ContatoService
import prj.applista.models.Contato
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class FormContatoActivity : AppCompatActivity() {

    lateinit var txtFormNome: EditText
    lateinit var txtFormTelefone: EditText
    lateinit var txtFormEmail: EditText
    lateinit var txtFormEndereco: EditText
    lateinit var btFormSalvar: Button
    lateinit var imgFotoContato: ImageView
    var contato: Contato? = null
    val TIRAR_FOTO = 1
    var caminhoFoto: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_contato)

        txtFormNome = findViewById(R.id.txtFormNome)
        txtFormEmail = findViewById(R.id.txtFormEmail)
        txtFormEndereco = findViewById(R.id.txtFormEndereco)
        txtFormTelefone = findViewById(R.id.txtFormTelefone)
        btFormSalvar = findViewById(R.id.btFormSalvar)
        imgFotoContato = findViewById(R.id.imgFotoContato)
        imgFotoContato.setOnClickListener { tirarFoto() }

        btFormSalvar.setOnClickListener { salvarContato() }

        if (intent.hasExtra("contato")) {
            contato = intent.getSerializableExtra("contato") as Contato
            exibirDadosContato()
        }

    }

    private fun tirarFoto() {
        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, TIRAR_FOTO)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TIRAR_FOTO) {
            if (resultCode == RESULT_OK) {
                val imageBitmap: Bitmap = data?.extras?.get("data") as Bitmap
                imgFotoContato.setImageBitmap(imageBitmap)
                caminhoFoto = salvarArquivo(imageBitmap)
            }
        }
    }

    private fun salvarArquivo(imageBitmap: Bitmap): String? {
        var caminho: String? = null
        var imageFolder = File(filesDir, "image")
        imageFolder.mkdirs()
        var timestamp = SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(Date())
        var imageFile = File(imageFolder, timestamp + ".jpg")

        try {
            val fileOut = FileOutputStream(imageFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOut)
            fileOut.close()
            caminho = imageFile.absolutePath
        } catch (e: Exception) {

            Log.e("Erro", "salvarArquivo: " + e.message)
        }
        Log.i("caminho arquivo", "salvarArquivo: " + caminho)
        return caminho

    }

    private fun exibirDadosContato() {

        txtFormNome.setText(contato!!.nome)
        txtFormTelefone.setText(contato!!.telefone)
        txtFormEmail.setText(contato!!.email)
        txtFormEndereco.setText(contato!!.endereco)
        if (contato!!.foto!="#"){
            imgFotoContato.setImageURI(Uri.parse(contato!!.foto))
        }

    }

    private fun salvarContato() {
        if (contato == null) {
            cadastrarContato()
        } else {
            atualizarContato()
        }
    }

    private fun atualizarContato() {
        var call = ApiClient.client.create(ContatoService::class.java)
        preencherDados()
        call.updateContato(contato!!._id, contato!!).enqueue(object : Callback<Contato> {
            override fun onResponse(call: Call<Contato>, response: Response<Contato>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@FormContatoActivity,
                        "Contato atualizado",
                        Toast.LENGTH_SHORT
                    ).show()
                    this@FormContatoActivity.finish()
                }
            }

            override fun onFailure(call: Call<Contato>, t: Throwable) {
                Toast.makeText(this@FormContatoActivity, "Erro ao atualizar", Toast.LENGTH_SHORT)
                    .show()
            }

        })


    }

    private fun preencherDados() {

        contato!!.nome = txtFormNome.text.toString()
        contato!!.endereco = txtFormEndereco.text.toString()
        contato!!.email = txtFormEmail.text.toString()
        contato!!.telefone = txtFormTelefone.text.toString()
        if (caminhoFoto!=null){
            contato!!.foto = caminhoFoto!!
        }
    }

    private fun cadastrarContato() {
        var _contato: Contato = lerDadosContato()
        var call = ApiClient.client.create(ContatoService::class.java)
        call.createContato(_contato).enqueue(object : Callback<Contato> {
            override fun onResponse(call: Call<Contato>, response: Response<Contato>) {

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@FormContatoActivity,
                        "Contato criado com sucesso",
                        Toast.LENGTH_LONG
                    ).show()
                    this@FormContatoActivity.finish()
                }
            }

            override fun onFailure(call: Call<Contato>, t: Throwable) {
                Log.e("Cadastro Contato", "onFailure: " + t.message)
            }

        })
    }

    private fun lerDadosContato(): Contato {

        var foto = "#"
        if (caminhoFoto!=null){
            foto = caminhoFoto as String
        }

        var _contato = Contato(
            _id = "",
            nome = txtFormNome.text.toString(),
            email = txtFormEmail.text.toString(),
            telefone = txtFormTelefone.text.toString(),
            endereco = txtFormEndereco.text.toString(),
            foto = foto
        )

        return _contato

    }
}