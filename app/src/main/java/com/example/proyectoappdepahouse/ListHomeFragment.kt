package com.example.proyectoappdepahouse

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.FragmentListHomeBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ListHomeFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var b:FragmentListHomeBinding
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
        b = FragmentListHomeBinding.inflate(inflater, container, false)
        val view = b.root

        getAll()

        return view
    }

    private fun getAll() {
        lstEstate = ArrayList()
        adapter = EstateAdapter(lstEstate)
        db.collection("estate")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val item = doc.toObject(Estate::class.java)
                    item.idestate = doc.id
                    item.name = doc["name"].toString()
                    item.district = doc["district"].toString()
                    item.price = (doc["price"] as? Double) ?: 0.0
                    item.photo = doc["photo"].toString()

                    b.listEstates.adapter = adapter
                    b.listEstates.layoutManager = LinearLayoutManager(requireContext())

                    lstEstate.add(item)
                }
            }

//          Forma 2
    //    storageReference = FirebaseStorage.getInstance().reference
//        val db = FirebaseFirestore.getInstance()
//        db.collection("estate")
//            .orderBy("name")
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val lst = ArrayList<Estate>()
//                    for (doc in task.result!!) {
//                        val estate = doc.toObject(Estate::class.java)
//                        lst.add(estate)
//                    }
//                    lstEstate.clear()
//                    lstEstate.addAll(lst)
//                    adapter.notifyDataSetChanged()
//                } else {
//                    Toast.makeText(this@MainActivity, "Ocurrió un error", Toast.LENGTH_SHORT).show()
//                }
//            }
    }


}