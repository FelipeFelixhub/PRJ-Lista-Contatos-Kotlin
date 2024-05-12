package prj.applista.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import prj.applista.R
import prj.applista.models.Contato

class ListaContatoAdapter(private val contatos: List<Contato>):
        RecyclerView.Adapter<ListaContatoAdapter.ContatoViewHolder>(){
    var posicaoClicada:Int = -1


            class ContatoViewHolder(v: View) : RecyclerView.ViewHolder(v){
                var nome : TextView
                var telefone: TextView
                var foto: ImageView

                init {
                    nome = v.findViewById(R.id.txtNome)
                    telefone = v.findViewById(R.id.txtTelefone)
                    foto = v.findViewById(R.id.imgFoto)



                    v.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
                        run{
                        val menuInflater:MenuInflater = MenuInflater(view.context)
                        menuInflater.inflate(R.menu.item_menu,contextMenu)

                    }}

                }

                fun setPosition(position: Int){
                    this.position = position
                }

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contato,parent,false)
        return ContatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val nome:TextView = holder.itemView.findViewById(R.id.txtNome)
        val telefone:TextView = holder.itemView.findViewById(R.id.txtTelefone)
        val foto = holder.foto

        nome.setText(contatos.get(position).nome)
        telefone.setText(contatos.get(position).telefone)
        if(contatos.get(position).foto!= "#"){
            foto.setImageURI(Uri.parse(contatos.get(position).foto))
        }

        holder.itemView.setOnLongClickListener{ v ->
            posicaoClicada = holder.adapterPosition
            Log.i("Menu","onBindViewHolder: " + posicaoClicada)
            false

        }

    }


    override fun getItemCount(): Int {
        return contatos.count()
    }

}
