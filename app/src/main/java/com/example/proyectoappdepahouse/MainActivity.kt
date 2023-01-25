package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectoappdepahouse.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var b: ActivityMainBinding

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
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
}