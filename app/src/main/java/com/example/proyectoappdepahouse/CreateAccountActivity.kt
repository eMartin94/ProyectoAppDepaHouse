package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectoappdepahouse.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var b: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.txtLogin.setOnClickListener {

            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }
}