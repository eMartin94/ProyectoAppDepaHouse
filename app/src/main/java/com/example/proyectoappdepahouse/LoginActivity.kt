package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.proyectoappdepahouse.databinding.ActivityLoginBinding
import com.example.proyectoappdepahouse.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var b:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        auth = Firebase.auth

        b.btnIniciarsesion.setOnClickListener {

            val mEmail = b.edtEmail.text.toString()
            val mPassword = b.edtPassword.text.toString()

            val user = User(mEmail, mPassword)

            when {

                mEmail.isEmpty() || mPassword.isEmpty() -> {

                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    SignIn(user)

                }
            }
        }

        b.txtRegister.setOnClickListener {

            val i = Intent(this, CreateAccountActivity::class.java)
            startActivity(i)
        }

        b.txtRecoveryPass.setOnClickListener {

            val i = Intent(this, RecoveryAccountActivity::class.java)
            startActivity(i)
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    private fun SignIn(user: User) {

        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    reload()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    private fun reload() {

        val i = Intent(this, MainActivity::class.java)
        this.startActivity(i)
    }
}