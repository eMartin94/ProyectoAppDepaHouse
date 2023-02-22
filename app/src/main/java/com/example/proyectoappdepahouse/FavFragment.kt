package com.example.proyectoappdepahouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.FragmentFavBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FavFragment : Fragment() {

    private lateinit var b: FragmentFavBinding
    val db = FirebaseFirestore.getInstance()
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
        b = FragmentFavBinding.inflate(inflater, container, false)
        val view = b.root

        getFavorites()

        return view
    }

    private fun getFavorites() {
        lstEstate = ArrayList()
        adapter = EstateAdapter(lstEstate)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("users").document(userId!!).collection("favorites")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                lstEstate.clear()
                for (doc in value!!) {
                    val item = doc.toObject(Estate::class.java)
                    item.idestate = doc.id
                    item.isLiked = true

                    lstEstate.add(item)
                }

                adapter.updateFavorites(lstEstate)
            }
        b.listFavorites.adapter = adapter
        b.listFavorites.layoutManager = LinearLayoutManager(requireContext())
    }


}