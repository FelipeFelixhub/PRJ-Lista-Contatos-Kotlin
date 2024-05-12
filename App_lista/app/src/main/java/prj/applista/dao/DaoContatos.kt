package prj.applista.dao

import prj.applista.models.Contato

class DaoContatos {
    fun ListaContatos():ArrayList<Contato>{
        val list:ArrayList<Contato> = ArrayList()
        list.add(Contato("id","Gordola", "gordola@bigode.com.br","123456789","Av. Antônia Rosa Fioravanti, 804, Mauá","#"))

        return list
    }
}