package com.example.proyecto_iurclothes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_iurclothes.adapter.AdaptadorPrendas
import com.example.proyecto_iurclothes.databinding.ActivitySecondBinding
import com.example.proyecto_iurclothes.model.Prenda
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var adaptadorPrendas: AdaptadorPrendas
    private var listaPrendas = ArrayList<Prenda>()
    private var listaPrendasCopia = ArrayList<Prenda>()
    private val db = FirebaseFirestore.getInstance()
    //Instancia del usuario logeado
     private var email = FirebaseAuth.getInstance().currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del RecyclerView
        adaptadorPrendas = AdaptadorPrendas(listaPrendas, this)
        binding.recyclerPrendas.layoutManager = LinearLayoutManager(this)
        binding.recyclerPrendas.adapter= adaptadorPrendas

        //Cargar datos de la base de datos
        cargarPrendasDesdeFirebase()
        //Spinner para filtrar en el recyclerView
        binding.spinnerTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val tipoSeleccionado = parent!!.adapter.getItem(position).toString()
                filtrarPrendasPorTipo(tipoSeleccionado)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Vacío para que devuelva todas las prendas al entrar a la pantalla
            }
        }
    }
    //Función para devolver las prendas de la base de datos en forma de lista
    private fun cargarPrendasDesdeFirebase() {
        db.collection("usuarios").document(email).collection("prendas")
            .get()
            .addOnSuccessListener { result ->
                listaPrendas.clear()
                listaPrendasCopia.clear()
                for (document in result) {
                    val prenda = document.toObject(Prenda::class.java)
                    listaPrendas.add(prenda)
                    listaPrendasCopia.add(prenda)
                }
                adaptadorPrendas.notifyDataSetChanged()
            }
            .addOnFailureListener {
            }
    }
    //Función para determinar el filtro aplicado a una segunda lista que devolverá con la elección del spinner
    private fun filtrarPrendasPorTipo(tipoSeleccionado: String) {
        val tipoSeleccionadoLowerCase = tipoSeleccionado.toLowerCase()
        if (tipoSeleccionado.isNotEmpty()) {
            // Filtrar la lista de prendas por el tipo seleccionado
            val listaFiltrada = listaPrendasCopia.filter { it.tipo.toLowerCase() == tipoSeleccionadoLowerCase }
            adaptadorPrendas.actualizarLista(listaFiltrada)
        } else {
            // Si no se ha selecciona nada, muestra la lista completa
            adaptadorPrendas.actualizarLista(listaPrendas)
        }
    }
}

