package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectoappdepahouse.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var b:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.txtRegister.setOnClickListener {

            val i = Intent(this, CreateAccountActivity::class.java)
            startActivity(i)
        }

    }
}