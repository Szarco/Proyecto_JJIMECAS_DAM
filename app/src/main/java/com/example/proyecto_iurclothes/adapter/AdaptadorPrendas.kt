package com.example.proyecto_iurclothes.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_iurclothes.InfoActivity
import com.example.proyecto_iurclothes.R
import com.example.proyecto_iurclothes.model.Prenda

class AdaptadorPrendas (var lista: ArrayList<Prenda>, var contexto: Context) : RecyclerView.Adapter<AdaptadorPrendas.PrendaHolder>(){

    // Patron del recyclerView
    class PrendaHolder(vista: View): RecyclerView.ViewHolder(vista){
        //Extraer cada elemento del patron -> imagen, textview
        var imagenImageView: ImageView
        var textoTextView: TextView
        //Inicializar
        init {
            imagenImageView = vista.findViewById(R.id.imagen_fila)
            textoTextView = vista.findViewById(R.id.nombre_fila)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrendaHolder {
        // Asociar la parte gráfica (XML - aspecto de la fila) con el patron.
        val vista: View = LayoutInflater.from(contexto).inflate(R.layout.item_prenda,parent,false)
        return PrendaHolder(vista);
    }

    override fun getItemCount(): Int {
        // Indica cuantos elementos tiene la lista.
        return lista.size
    }
    //Función para añadir una prenda a la lista
    fun addPrenda(prenda: Prenda): Unit {
        lista.add(prenda)
        notifyItemInserted(lista.size-1)
    }

    //Función para actualizar la lista de prendas
    fun actualizarLista(nuevaLista: List<Prenda>) {
        lista.clear()
        lista.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PrendaHolder, position: Int) {
        // Representa cada elemento de la lista en su posicion (utilizando el patron)
        val item = lista[position]
        holder.textoTextView.text = item.tipo
        //Libreria Glide para cargar la imagen
        Glide.with(contexto).load(item.imagen).into(holder.imagenImageView)
        holder.imagenImageView.setOnClickListener {
            val intent = Intent(contexto, InfoActivity::class.java)
            intent.putExtra("prenda_id", lista[position].id)
            intent.putExtra("prenda", lista[position])
            contexto.startActivity(intent)
        }
    }
}