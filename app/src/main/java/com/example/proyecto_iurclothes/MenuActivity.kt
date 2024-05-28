package com.example.proyecto_iurclothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyecto_iurclothes.databinding.ActivityMenuBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var autenticar: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Autentificación inicializada
        autenticar = Firebase.auth

    }
    override fun onStart() {
        super.onStart()
        //Acción para acceder a la pantalla para registrar una prenda
        binding.prendas.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        //Acción para acceder a la pantalla para consultar una prenda
        binding.armario.setOnClickListener{
            val intent = Intent(applicationContext, SecondActivity::class.java)
            startActivity(intent)
        }
        //Acción para acceder a la pantalla para buscar un conjunto aleatorio de prendas
        binding.outfit.setOnClickListener{
            val intent = Intent(applicationContext, ThirdActivity::class.java)
            startActivity(intent)
        }
        //Acción para cerrar sesión de la autentificación previa
        binding.cerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //Para volver al registro o inicio de sesión
            val intent = Intent(applicationContext, UserActivity::class.java)
            startActivity(intent)

        }
    }
}