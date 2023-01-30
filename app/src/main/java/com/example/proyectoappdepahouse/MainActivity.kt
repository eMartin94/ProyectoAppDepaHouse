package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
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
        replaceFragment(ListHomeFragment())

        auth = Firebase.auth
        val displayName = intent.getStringExtra("displayName")
        b.txtNameuser.text = displayName

        b.btnSignout.setOnClickListener {
            signOut()
        }


        b.contBtnMenu.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.home_dest -> replaceFragment(ListHomeFragment())
                R.id.fav_dest -> replaceFragment(FavFragment())
                R.id.profile_dest -> {
                    val user = auth.currentUser
                    if (user != null) {
                        val bundle = Bundle()
                        bundle.putString("email", user.email ?: "")
                        bundle.putString("username", user.displayName ?: "")
                        val profileImageUri = user.photoUrl
                        if (profileImageUri != null) {
                            bundle.putString("photoUrl", profileImageUri.toString())
                        } else {
                            bundle.putString("photoUrl", "")
                        }
                        val fragment = ProfileFragment()
                        fragment.arguments = bundle
                        replaceFragment(fragment)
                    } else {
                        val email = auth.currentUser?.email
                        val name = auth.currentUser?.displayName
                        if (email != null) {
                            val bundle = Bundle()
                            bundle.putString("email", email)
                            bundle.putString("username", name)
                            bundle.putString("photoUrl", "")
                            val fragment = ProfileFragment()
                            fragment.arguments = bundle
                            replaceFragment(fragment)
                        }
                    }
                }

                else -> {

                }
            }

            true

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

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_Container, fragment)
        fragmentTransaction.commit()
    }

//    private fun updateUI() {
//
//        val user = auth.currentUser
//
//        if (user != null) {
//
//            val bundle = Bundle()
//            bundle.putString("email", user.email)
//            bundle.putString("name", user.displayName)
//            val profileImageUri = user.photoUrl
//            if (profileImageUri != null) {
//                bundle.putString("photoUrl", profileImageUri.toString())
//            }
//            val fragment = ProfileFragment()
//            fragment.arguments = bundle
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_Container, fragment)
//                .commit()
//        }
//    }
}
