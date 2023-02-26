package com.example.proyectoappdepahouse

import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proyectoappdepahouse.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        replaceFragment(ListHomeFragment())

        hideKeyboard()

        auth = Firebase.auth
        val currentUser = auth.currentUser
        val uid = currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val displayName = intent.getStringExtra("displayName")

        db.collection("users").document(uid).get().addOnSuccessListener {

            val nameUser = (it.get("nameUser") as String?)
            var name = ""
            name = if (displayName != null) {
                displayName
            } else if (nameUser != null) {
                nameUser
            } else {

                currentUser.displayName ?: ""
            }

            Toast.makeText(this, "Bienvenido(a) $name", Toast.LENGTH_LONG).show()
        }


        b.contBtnMenu.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.home_dest -> {
                    onPause()
                    replaceFragment(ListHomeFragment())
                }

                R.id.fav_dest -> {
                    onPause()
                    replaceFragment(FavFragment())

                }
                R.id.info_dest -> {
                    onPause()
                    replaceFragment(InfoListFragment())

                }
                R.id.profile_dest -> {
                    onPause()
                    val user = auth.currentUser
                    val uid = currentUser!!.uid
                    val db = FirebaseFirestore.getInstance()
                    if (user != null) {
                        val bundle = Bundle()
                        bundle.putString("email", user.email)
                        bundle.putString("name", user.displayName)
                        val profileImageUri = user.photoUrl
                        if (profileImageUri != null) {
                            bundle.putString("photoUrl", profileImageUri.toString())
                        }

                        db.collection("users").document(uid).get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val username = documentSnapshot.getString("nameUser")
                                    val email = documentSnapshot.getString("email")
                                    bundle.putString("nameUser", username)
                                    bundle.putString("email", email)
                                }
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

        b.btnCrearEstate.setOnClickListener {
            var i = Intent(this, CreateEstateActivity::class.java)
            startActivity(i)
        }

        if (currentUser != null) {
            val useremail = currentUser.email
            if (useremail != "admin@gmail.com") {
                b.btnCrearEstate.visibility = View.GONE
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_Container, fragment)
        fragmentTransaction.commit()
    }

    fun hideKeyboard() {
        hideSoftKeyboard(this, findViewById(android.R.id.content))
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
