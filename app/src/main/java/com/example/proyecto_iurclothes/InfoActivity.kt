package com.example.proyecto_iurclothes

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_iurclothes.databinding.ActivityInfoBinding
import com.example.proyecto_iurclothes.model.Prenda
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var prenda: Prenda
    private val db = FirebaseFirestore.getInstance()
    //Instancia del usuario logeado
    private var email = FirebaseAuth.getInstance().currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del adaptador para el spinner de Tipo
        val tipoAdapter = ArrayAdapter.createFromResource(this, R.array.tipo_spinner, android.R.layout.simple_spinner_item)
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTipo.adapter = tipoAdapter

        // Configuración del adaptador para el spinner de Color
        val colorAdapter = ArrayAdapter.createFromResource(this, R.array.color_spinner, android.R.layout.simple_spinner_item)
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerColor.adapter = colorAdapter

        // Configuración del adaptador para el spinner de Temporada
        val temporadaAdapter = ArrayAdapter.createFromResource(this, R.array.temp_spinner, android.R.layout.simple_spinner_item)
        temporadaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTemp.adapter = temporadaAdapter

        // Configuración del adaptador para el spinner de Moda
        val modaAdapter = ArrayAdapter.createFromResource(this, R.array.moda_spinner, android.R.layout.simple_spinner_item)
        modaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerModa.adapter = modaAdapter

        // Recuperar la prenda seleccionada del intent
        prenda = intent.getSerializableExtra("prenda") as Prenda

        // Mostrar la imagen de la prenda en el ImageView
        Glide.with(this).load(prenda.imagen).into(binding.foto)

        // Cargar los datos de la prenda en los spinners
        cargarDatosPrenda()

        //Acción para actualizar la información de la prenda
        binding.actualizar.setOnClickListener{
            var prenda = Prenda(
                id = prenda.id,
                imagen = prenda.imagen, // La URL de la imagen no se modifica
                tipo = binding.spinnerTipo.selectedItem.toString(),
                temporada = binding.spinnerTemp.selectedItem.toString(),
                moda = binding.spinnerModa.selectedItem.toString(),
                color = binding.spinnerColor.selectedItem.toString(),
                favorito = binding.favorito.isChecked
            )
            // Actualizar los datos en la base de datos
            db.collection("usuarios").document(email).collection("prendas").whereEqualTo("imagen", prenda.imagen) // Suponiendo que cada prenda tiene un ID único en la base de datos
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Actualizar el documento
                        db.collection("usuarios").document(email).collection("prendas").document(document.id).set(prenda)
                            .addOnSuccessListener {
                                Snackbar.make(binding.root, "Prenda actualizada", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()
                            }
                            .addOnFailureListener { e ->
                                Snackbar.make(binding.root, "Error al actualizar la prenda", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()
                            }
                    }
                }
        }
        //Acción para borrar una prenda completamente
        binding.borrar.setOnClickListener {
            // Borrar la imagen del almacenamiento
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(prenda.imagen)
            storageReference.delete().addOnSuccessListener {
                // Borrado exitoso
                // Recorrer la base de datos para encontrar y eliminar las referencias a la imagen borrada
                db.collection("usuarios").document(email).collection("prendas").whereEqualTo("imagen", prenda.imagen).get()
                    .addOnSuccessListener { documents ->
                        //Recorrer la base de datos
                        for (document in documents) {
                            // Eliminar el documento
                            db.collection("usuarios").document(email).collection("prendas").document(document.id).delete().addOnSuccessListener {
                                Snackbar.make(binding.root, "Prenda eliminada", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {
                                    val intent = Intent(applicationContext, MenuActivity::class.java)
                                    startActivity(intent)
                                }.show()
                                }
                        }
                    }
            }
        }
    }
    //Función para cargar la información de la prenda
    private fun cargarDatosPrenda() {
        // Cargar los datos de la prenda en los spinners desde la base de datos
        binding.spinnerTipo.setSelection(obtenerIndiceSpinner(binding.spinnerTipo, prenda.tipo))
        binding.spinnerTemp.setSelection(obtenerIndiceSpinner(binding.spinnerTemp, prenda.temporada))
        binding.spinnerModa.setSelection(obtenerIndiceSpinner(binding.spinnerModa, prenda.moda))
        binding.spinnerColor.setSelection(obtenerIndiceSpinner(binding.spinnerColor, prenda.color))
        binding.favorito.isChecked = prenda.favorito
    }
    //Función para buscar el indice seleccionado en el spinner
    private fun obtenerIndiceSpinner(spinner: Spinner, valor: String): Int {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == valor) {
                return i
            }
        }
        return 0
    }
}





