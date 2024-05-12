package prj.applista

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import prj.applista.models.Contato

class DetalhesContatoActivity : AppCompatActivity() {

    var contato:Contato?=null
    var txtNome:EditText?=null
    var txtEmail:EditText?=null
    var txtTelefone:EditText?=null
    var txtEdereco:EditText?=null
    var imgFoto:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_contato)

        txtNome = findViewById(R.id.txtNome)
        txtEmail = findViewById(R.id.txtEmail)
        txtTelefone = findViewById(R.id.txtTelefone)
        txtEdereco = findViewById(R.id.txtEndereco)
        imgFoto = findViewById(R.id.imgFoto)


        if (this.intent.hasExtra("contato"))
            contato = this.intent.getSerializableExtra("contato") as Contato?
            preencherContato()
    }

    private fun preencherContato() {
        txtNome?.setText(contato?.nome)
        txtEmail?.setText(contato?.email)
        txtTelefone?.setText(contato?.telefone)
        txtEdereco?.setText((contato?.endereco))

        if (contato?.foto != "#" ){
            imgFoto?.setImageURI(Uri.parse(contato?.foto))
        }

    }
}