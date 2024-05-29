package com.example.proyecto_iurclothes


import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.proyecto_iurclothes.databinding.ActivityMainBinding
import com.example.proyecto_iurclothes.model.Prenda
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //Base de datos, firestore
    private val db = FirebaseFirestore.getInstance()
    //Storage para almacenamiento de imagenes
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var storageReference: StorageReference
    //Ruta de la imagen para guardarla en el objeto prenda
    private lateinit var imagenUrl :String
    //Contador de id´s
    private var id: Int = 0
    //Instancia del usuario logeado
    private var email = FirebaseAuth.getInstance().currentUser?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carpeta "prendas" en Firebase Storage
        storageReference = storage.reference.child("prendas")

        // Obtener el último ID de la base de datos Firebase
        ultimoIdPrenda()

        //Acción para lanzar la cámara
        binding.camara.setOnClickListener {
            //Guardar externamente el archivo temporal de imagen
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                it.resolveActivity(packageManager).also { component ->
                    archivoImagen()
                    val fotoUri: Uri = FileProvider.getUriForFile(
                            this,  "com.example.proyecto_iurclothes"+ ".fileprovider", file)
                    it.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri)
                }
            }
            // Para iniciar la actividad de la cámara
            abrirCamara.launch(intent)
        }
        //Acción para guardar un objeto prenda y sus características
        binding.guardar.setOnClickListener {
            //Contador de id´s, incrementa cada vez que se guarda una prenda
            id = id+1
            var prenda = Prenda(
                id= id,
                imagen = imagenUrl,
                tipo = binding.spinnerTipo.selectedItem.toString(),
                temporada = binding.spinnerTemp.selectedItem.toString(),
                moda = binding.spinnerModa.selectedItem.toString(),
                color = binding.spinnerColor.selectedItem.toString(),
                favorito = binding.favorito.isChecked
            )
            //Llamar a la función para guardar la prenda
            guardarPrendaEnFirebase(prenda)
        }
    }
    //Actividad registrada de la cámara
    val abrirCamara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        //Resultado de la actividad de la cámara
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val bitmap = BitmapFactory.decodeFile(file.toString())
            //Mostrar la imagen en el imagenView
            binding.foto.setImageBitmap(bitmap)
            //Preparar y subir la imagen a Firebase Storage
            val imagenUri = Uri.fromFile(file)
            //Ubicación en FireStore
            val imagesRef: StorageReference = storageReference.child(UUID.randomUUID().toString())
            //Subir imagen a FireStore
            val subirImagen = imagesRef.putFile(imagenUri)
            subirImagen.addOnSuccessListener { imagenCapturada ->
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    //Almacenar la ruta de la imagen
                    imagenUrl = uri.toString()
                }
            }
        }
    }
    //Crear un archivo temporal de la imagen
    private lateinit var file: File
    private fun archivoImagen() {
        //Variable para almacenar la imagen en almacenamiento externo
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        //Objeto de tipo file temporal
        file = File.createTempFile("IMG_${System.currentTimeMillis()}_", ".jpg", dir)
    }
    //Función para obtener el ultimo ID guardado de una prenda
    private fun ultimoIdPrenda() {
        db.collection("usuarios").document(email).collection("prendas").orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { documents ->
                //Recorrer los documentos para obtener el ultimo id
                for (document in documents) {
                    id = document.getLong("id")?.toInt() ?: 0
                }
            }
    }
    //Función que almacena un objeto prenda en la base de datos de firebase
    private fun guardarPrendaEnFirebase(prenda: Prenda) {
        db.collection("usuarios").document(email).collection("prendas")
            .add(prenda)
            .addOnSuccessListener {
                Snackbar.make(binding.root, "Prenda guardada", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {}.show()
            }
    }

}


