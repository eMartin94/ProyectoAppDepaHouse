package com.example.proyectoappdepahouse

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.adapter.InfoRequestAdapter
import com.example.proyectoappdepahouse.databinding.FragmentInfoListBinding
import com.example.proyectoappdepahouse.databinding.FragmentListHomeBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InfoListFragment : Fragment() {

//    private lateinit var db: FirebaseFirestore
//    private lateinit var adapter: InfoRequestAdapter
//    private lateinit var mContext: Context
    val db = FirebaseFirestore.getInstance()
    private lateinit var b: FragmentInfoListBinding
    private lateinit var lstInfoRequest: ArrayList<Estate>
    private lateinit var adapter: InfoRequestAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentInfoListBinding.inflate(inflater, container, false)
        val view = b.root

        lstInfoRequest = ArrayList()
        adapter = InfoRequestAdapter(lstInfoRequest)
        b.listInfo.layoutManager = LinearLayoutManager(context)
        b.listInfo.adapter = adapter
        getInfoRequests()

        return view

    }

    private fun getInfoRequests() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            db.collection("info_request")
                .whereEqualTo("userId", currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val name = document.get("name").toString()
                        val district = document.get("district").toString()
                        val city = document.get("city").toString()
                        val price = document.get("price") as? Double ?: 0.0
                        val photo = document.get("photo").toString()
//                        val infoRequest = Estate(name, district, city, price, photo)
                        val infoRequest = Estate(
                            name = name,
                            district = district,
                            city = city,
                            price = price,
                            photo = photo
                        )
                        lstInfoRequest.add(infoRequest)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Mostrar mensaje de error al usuario
                }
        }
    }




}