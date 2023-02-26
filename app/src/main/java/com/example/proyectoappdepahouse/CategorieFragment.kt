package com.example.proyectoappdepahouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappdepahouse.adapter.CategorieEstAdapter
import com.example.proyectoappdepahouse.databinding.FragmentCategorieBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.firestore.FirebaseFirestore

class CategorieFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var b: FragmentCategorieBinding
    private lateinit var lstEstate: ArrayList<Estate>
    private lateinit var adapter: CategorieEstAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentCategorieBinding.inflate(inflater, container, false)
        val view = b.root

        val text = arguments?.getString("text", "")
        b.txtType.text = text

        getDepartament()

        return view
    }


    private fun getDepartament() {

        val text = arguments?.getString("text", "")
        b.txtType.text = text
        val type = b.txtType.text

        lstEstate = ArrayList()
        adapter = CategorieEstAdapter(lstEstate)
        db.collection("estate").get().addOnSuccessListener { documents ->
                for (doc in documents) {
                    if (doc["type"] == type) {
                        val item = doc.toObject(Estate::class.java)
                        item.idestate = doc.id
                        item.name = doc["name"].toString()
                        item.district = doc["district"].toString()
                        item.city = doc["city"].toString()
//                        item.location = doc["location"].toString()
                        item.price = (doc["price"] as? Double) ?: 0.0
                        item.photo = doc["photo"].toString()

                        item.location = doc["location"] as Map<String, Double>

                        b.listCategories.adapter = adapter
                        b.listCategories.layoutManager = LinearLayoutManager(requireContext())
                        lstEstate.add(item)
                    }
                }
            }
    }


}