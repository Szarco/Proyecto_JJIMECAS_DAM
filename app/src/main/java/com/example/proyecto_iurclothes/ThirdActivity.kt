package com.example.proyecto_iurclothes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.proyecto_iurclothes.databinding.ActivityThirdBinding
import com.example.proyecto_iurclothes.model.Prenda
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ThirdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdBinding
    private val db = FirebaseFirestore.getInstance()
    private var listaPrendas = ArrayList<Prenda>()
    //Instancia del usuario logeado
    private var email = FirebaseAuth.getInstance().currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar adaptador para el spinner de la parte superior
        val adapterSuperior = ArrayAdapter.createFromResource(this, R.array.superior_spinner, android.R.layout.simple_spinner_item)
        adapterSuperior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSuperior.adapter = adapterSuperior

        // Configurar adaptador para el spinner de la parte inferior
        val adapterInferior = ArrayAdapter.createFromResource(this, R.array.inferior_spinner, android.R.layout.simple_spinner_item)
        adapterInferior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerInferior.adapter = adapterInferior

        // Configurar adaptador para el spinner del calzado
        val adapterCalzado = ArrayAdapter.createFromResource(this, R.array.calzado_spinner, android.R.layout.simple_spinner_item)
        adapterCalzado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCalzado.adapter = adapterCalzado

        //Acción para fijar la imagen correspondiente a la parte superior
        binding.fijarSup.setOnClickListener {
            binding.fijarSup.isActivated=true
            Snackbar.make(binding.root, "Parte superior fijada", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()
        }
        //Acción para fijar la imagen correspondiente a la parte inferior
        binding.fijarInf.setOnClickListener {
            binding.fijarInf.isActivated=true
            Snackbar.make(binding.root, "Parte inferior fijada", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()
        }
        //Acción para fijar la imagen correspondiente al calzado
        binding.fijarCal.setOnClickListener {
            binding.fijarCal.isActivated=true
            Snackbar.make(binding.root, "Calzado fijado", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()

        }
        //Acción para generar las imagenes aleatorias
        binding.crear.setOnClickListener {
            generarImagenAleatoria()
        }
        cargarPrendasDesdeFirestore()

    }
    //Función para cargar todas las prendas de la base de datos
    private fun cargarPrendasDesdeFirestore() {
        db.collection("usuarios").document(email).collection("prendas").get().addOnSuccessListener { result ->
            listaPrendas.clear()
            for (document in result) {
                val prenda = document.toObject(Prenda::class.java)
                listaPrendas.add(prenda)
            }
        }
            .addOnFailureListener {
                Snackbar.make(binding.root, "No hay prendas registradas", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()
            }
    }
    private fun generarImagenAleatoria() {
        // Obtener los tipos seleccionados en los spinners
        val tipoSeleccionadoSuperior = binding.spinnerSuperior.selectedItem.toString()
        val tipoSeleccionadoInferior = binding.spinnerInferior.selectedItem.toString()
        val tipoSeleccionadoCalzado = binding.spinnerCalzado.selectedItem.toString()

        // Filtrar las prendas para cada tipo
        val prendasSuperior = listaPrendas.filter { it.tipo == tipoSeleccionadoSuperior }
        val prendasInferior = listaPrendas.filter { it.tipo == tipoSeleccionadoInferior }
        val prendasCalzado = listaPrendas.filter { it.tipo == tipoSeleccionadoCalzado }

        // Verificar si hay prendas disponibles después del filtrado
        if (prendasSuperior.isNotEmpty() && prendasInferior.isNotEmpty() && prendasCalzado.isNotEmpty()) {
            if(binding.fijarSup.isActivated){
                if(binding.fijarInf.isActivated){
                    // Seleccionar una prenda aleatoria para un tipo
                    val prendaAleatoriaCalzado = prendasCalzado.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaCalzado.imagen, binding.imagen3)
                }
                else if(binding.fijarCal.isActivated){
                    // Seleccionar una prenda aleatoria para un tipo
                    val prendaAleatoriaInferior = prendasInferior.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaInferior.imagen, binding.imagen2)
                }
                else{
                    // Seleccionar una prenda aleatoria para dos tipos
                    val prendaAleatoriaInferior = prendasInferior.random()
                    val prendaAleatoriaCalzado = prendasCalzado.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaInferior.imagen, binding.imagen2)
                    cargarImagenDesdeUrl(prendaAleatoriaCalzado.imagen, binding.imagen3)
                }
            }
            else if(binding.fijarInf.isActivated) {
                if(binding.fijarSup.isActivated){
                    // Seleccionar una prenda aleatoria para un tipo
                    val prendaAleatoriaCalzado = prendasCalzado.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaCalzado.imagen, binding.imagen3)
                }
                else if(binding.fijarCal.isActivated){
                    // Seleccionar una prenda aleatoria para un tipo
                    val prendaAleatoriaSuperior = prendasSuperior.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaSuperior.imagen, binding.imagen1)
                }
                else{
                    // Seleccionar una prenda aleatoria para dos tipos
                    val prendaAleatoriaSuperior = prendasSuperior.random()
                    val prendaAleatoriaCalzado = prendasCalzado.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaSuperior.imagen, binding.imagen1)
                    cargarImagenDesdeUrl(prendaAleatoriaCalzado.imagen, binding.imagen3)
                }
            }
            else if(binding.fijarCal.isActivated) {
                if(binding.fijarSup.isActivated){
                    // Seleccionar una prenda aleatoria para un tipo
                    val prendaAleatoriaInferior = prendasInferior.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaInferior.imagen, binding.imagen2)
                }
                else if(binding.fijarInf.isActivated){
                    // Seleccionar una prenda aleatoria para un tipo
                    val prendaAleatoriaSuperior = prendasSuperior.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaSuperior.imagen, binding.imagen1)
                }
                else {
                    // Seleccionar una prenda aleatoria para dos tipos
                    val prendaAleatoriaSuperior = prendasSuperior.random()
                    val prendaAleatoriaInferior = prendasInferior.random()
                    // Mostrar las imágenes aleatorias
                    cargarImagenDesdeUrl(prendaAleatoriaSuperior.imagen, binding.imagen1)
                    cargarImagenDesdeUrl(prendaAleatoriaInferior.imagen, binding.imagen2)
                }
            }
            else {
                // Seleccionar una prenda aleatoria para cada tipo
                val prendaAleatoriaSuperior = prendasSuperior.random()
                val prendaAleatoriaInferior = prendasInferior.random()
                val prendaAleatoriaCalzado = prendasCalzado.random()

                // Mostrar las imágenes aleatorias
                cargarImagenDesdeUrl(prendaAleatoriaSuperior.imagen, binding.imagen1)
                cargarImagenDesdeUrl(prendaAleatoriaInferior.imagen, binding.imagen2)
                cargarImagenDesdeUrl(prendaAleatoriaCalzado.imagen, binding.imagen3)
            }
        }else{
            //Mensaje de error
            Snackbar.make(binding.root, "Debes seleccionar opciones con prendas guardadas", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()
        }
    }
    // Función que usa Glide para cargar la imagen desde la URL y mostrarla en los ImageView
    private fun cargarImagenDesdeUrl(imageUrl: String, imageView: ImageView){
        Glide.with(this).load(imageUrl).into(imageView)
    }
}



