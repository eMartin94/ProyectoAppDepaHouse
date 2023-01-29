package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectoappdepahouse.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var b: ActivityMainBinding
    private lateinit var clientGoogle: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        auth = Firebase.auth

        b.btnSignout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {

        auth.signOut()
        finish()

        auth = Firebase.auth

        clientGoogle = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        clientGoogle.signOut()
            .addOnCompleteListener(this) {
                // Actualice la interfaz de usuario aquí
                reload()
            }

        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // Usuario conectado, actualizar interfaz de usuario
                reload()
            } else {
                // Usuario desconectado, actualizar interfaz de usuario
            }
        }

        auth.addAuthStateListener(authListener)
        auth.removeAuthStateListener(authListener)



    }

    private fun reload() {

        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
}