package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.proyectoappdepahouse.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var b: ActivityLoginBinding
    private lateinit var clientGoogle: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        auth = Firebase.auth

        val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        clientGoogle = GoogleSignIn.getClient(this, opt)

        b.btnIniciarsesion.setOnClickListener {

            val mEmail = b.edtEmail.text.toString()
            val mPassword = b.edtPassword.text.toString()

            when {
                mEmail.isEmpty() || mPassword.isEmpty() -> {
                    Toast.makeText(
                        baseContext, "Correo o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    signIn(mEmail, mPassword)

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

        b.btnGoogle.setOnClickListener {

            val signInIntent = clientGoogle.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w("TAG", "No se pudo iniciar sesión", e)
                // Update UI accordingly
                Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión con éxito, actualizar UI con la información del usuario
                    updateUI()
                } else {
                    // Si falla el inicio de sesión, mostrar un mensaje de error al usuario
                    Toast.makeText(
                        baseContext, "Error al Iniciar Sessión con Google",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    updateUI()

                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "El usuario no existe",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }


    private fun updateUI() {

        val user = auth.currentUser

        if (user != null) {

            val i = Intent(this, MainActivity::class.java)
            i.putExtra("displayName", user.displayName)
            startActivity(i)

        }
    }


    companion object {
        private const val RC_SIGN_IN = 9001
    }
}