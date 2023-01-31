package com.example.proyectoappdepahouse

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var b: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        b = FragmentProfileBinding.inflate(inflater, container, false)

        val email = arguments?.getString("email")
        val name = arguments?.getString("name")
        val username = arguments?.getString("nameUser")
        val photoUrl = arguments?.getString("photoUrl")

        email?.let {
            b.txtEmail.text = it
        }

        val displayName = name ?: username
        displayName?.let {
            b.txtUserName.text = it
        }

        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .into(b.imgPhoto)
        }
        b.btnSignout.setOnClickListener {
            Toast.makeText(requireContext(), "Cerrar Sesión", Toast.LENGTH_SHORT).show()
            signOut()
        }

        return b.root
    }

    private fun signOut() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val clientGoogle = GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
        clientGoogle.signOut().addOnCompleteListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

}