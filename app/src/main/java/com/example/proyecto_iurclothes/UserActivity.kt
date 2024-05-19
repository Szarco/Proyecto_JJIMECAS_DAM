package com.example.proyecto_iurclothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyecto_iurclothes.databinding.ActivityUserBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var autenticar: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Autentificación inicializada
        autenticar = Firebase.auth

    }
    override fun onStart() {
        super.onStart()
                // Recordar datos de acceso
                val currentUser = autenticar.currentUser
                if (currentUser != null){
                    val intent = Intent(applicationContext, MenuActivity::class.java)
                    startActivity(intent)
                }
            //Acción para registrar el usuario
            binding.botonRegistrar.setOnClickListener {
                //Condiciones para registrar el usuario
                if (binding.editUsuario.text.isNotEmpty() && binding.editUsuario.text.contains("@") &&
                    binding.editUsuario.text.contains(".com") && binding.editPass.text.isNotEmpty()
                ) {
                    //Crear usuario en firebase
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.editUsuario.text.toString(),
                        binding.editPass.text.toString()
                    ).addOnCompleteListener {
                        //Mensaje si completa el registro
                        if (it.isSuccessful) {
                            Snackbar.make(binding.root, "Usuario registrado correctamente", Snackbar.LENGTH_LONG).show()
                        }
                        //Mensaje si falla el registro
                        else {
                            Snackbar.make(binding.root, "Error al realizar el registro", Snackbar.LENGTH_INDEFINITE).setAction("Entendido") {}.show()
                        }
                    }
                }
                //Mensaje si no cumple las condiciones para el registro
                else { Snackbar.make(binding.root, "Error al realizar el registro", Snackbar.LENGTH_INDEFINITE).setAction("Entendido") {}.show()
                }
            }
            //Acción para logear el usuario
            binding.botonLogin.setOnClickListener {
                //Condiciones de logeo del usuario
                if (binding.editUsuario.text.isNotEmpty() && binding.editUsuario.text.contains("@") &&
                    binding.editUsuario.text.contains(".com") && binding.editPass.text.isNotEmpty()
                ) {
                    //Comprobacion de usuario y contraseña para logear
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        binding.editUsuario.text.toString(),
                        binding.editPass.text.toString()
                    ).addOnCompleteListener {
                        //Login correcto
                        if (it.isSuccessful) {
                            val intent = Intent(applicationContext, MenuActivity::class.java)
                            startActivity(intent)
                        }
                        //Login incorrecto
                        else {
                            Snackbar.make(binding.root, "Error al logear", Snackbar.LENGTH_INDEFINITE).setAction("Entendido") {}.show()
                        }
                    }
                }
                //Comprobación de usuario y contraseña incorrectos
                else {
                    Snackbar.make(binding.root, "Error en datos de acceso", Snackbar.LENGTH_SHORT).show()
                }
            }
            //Acción para recuperar la contraseña
            binding.botonRecuperar.setOnClickListener {
                val email = binding.editUsuario.text.toString()
                //Comprobación del email para enviar el mensaje de recuperación
                if (email.isNotEmpty() && email.contains("@") && email.contains(".com")) {
                    autenticar.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            //Email correcto para solicitar recuperación de contraseña
                            if (task.isSuccessful) {
                                Snackbar.make(binding.root, "Se ha enviado un correo electrónico para restablecer la contraseña.", Snackbar.LENGTH_SHORT).show()
                            }
                            //Email incorrecto para solicitar recuperación de contraseña
                            else {
                                Snackbar.make(binding.root, "Error al enviar el correo electrónico para restablecer la contraseña.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                }
                //Comprobación del email para enviar el mensaje de recuperación
                else {
                    Snackbar.make(binding.root, "Ingrese un correo electrónico válido.", Snackbar.LENGTH_SHORT).show()
                }
            }

    }
}