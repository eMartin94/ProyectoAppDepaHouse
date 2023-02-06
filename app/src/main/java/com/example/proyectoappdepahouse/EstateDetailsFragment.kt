package com.example.proyectoappdepahouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.FragmentEstateDetailsBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.firestore.FirebaseFirestore

class EstateDetailsFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var b: FragmentEstateDetailsBinding
    private lateinit var lstEstate: ArrayList<Estate>
    private lateinit var adapter: EstateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentEstateDetailsBinding.inflate(inflater, container, false)
        val view = b.root

        val estate = arguments?.getSerializable("estate") as Estate

        b.txtDetailsDescription.text = estate.name
        b.txtDetailsLocation.text = estate.district + ", " + estate.city
        b.txtDetailsPrice.text = "s/ ${String.format("%.2f", estate.price ?: 0.0)}"
        if (estate.photo != null) {
            Glide.with(this)
                .load(estate.photo)
                .into(b.imgDetailsPhoto)
        }

        return view
    }

}