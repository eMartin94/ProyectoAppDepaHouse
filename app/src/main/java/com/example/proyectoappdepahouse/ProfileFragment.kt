package com.example.proyectoappdepahouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.databinding.FragmentProfileBinding

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
        val name = arguments?.getString("username")
        val photoUrl = arguments?.getString("photoUrl")

        email?.let {
            b.txtEmail.text = it
        }
        name?.let {
            b.txtUserName.text = it
        }

        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .into(b.imgPhoto)
        }

        return b.root
    }

}