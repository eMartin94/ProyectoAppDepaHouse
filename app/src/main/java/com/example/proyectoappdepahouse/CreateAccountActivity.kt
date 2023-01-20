package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.proyectoappdepahouse.databinding.ActivityCreateAccountBinding
import com.example.proyectoappdepahouse.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseFirestore
    private lateinit var b: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(b.root)

        auth = Firebase.auth

        b.btnRegistrar.setOnClickListener {

            val mNameUser = b.edtUsername.text.toString()
            val mEmail = b.edtEmail.text.toString().trim()
            val mPassword = b.edtPassword.text.toString()
            val mRepeatPassword = b.edtConfirmPassword.text.toString()
            val passwordRegex = Pattern.compile(
                "^" +
                        "(?=.*[-@#$%^&+=])" +   // Al menos 1 caracter especial
                        ".{6,}" +               // Al menos 4 caracteres
                        "$"
            )

            val user = User(mNameUser, mEmail, mPassword)

            if (mNameUser.isEmpty() || mNameUser.length < 5) {
                Toast.makeText(baseContext, "Ingrese un usuario váilido válido",
                    Toast.LENGTH_SHORT).show()

            } else if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(baseContext, "Ingrese un correo válido",
                    Toast.LENGTH_SHORT).show()

            } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()) {
                Toast.makeText(baseContext, "La contraseña es debil",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword) {
                Toast.makeText(baseContext, "Confirma la contraseña",
                    Toast.LENGTH_SHORT).show()
            } else {

                createAccount(user)
            }


        }

        b.txtLogin.setOnClickListener {

            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }


    private fun createAccount(user: User) {

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val db = FirebaseFirestore.getInstance()
                    val newUser = hashMapOf(
                        "nameUser" to user.nameUser,
                        "email" to user.email
                    )
                    db.collection("users").document(currentUser!!.uid)
                        .set(newUser)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario creado satisfactoriamente", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Ha ocurrido un erro", Toast.LENGTH_LONG).show()
                }
            }
    }

}