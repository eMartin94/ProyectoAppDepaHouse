package com.example.proyectoappdepahouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.FragmentListHomeBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ListHomeFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var b: FragmentListHomeBinding
    private lateinit var lstEstate: ArrayList<Estate>
    internal lateinit var adapter: EstateAdapter


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

//        b.btnBuscar.setOnClickListener {
//            val searchTerm = b.edtBuscar.text.toString().trim()
//            if (searchTerm.isNotEmpty()) {
//                search(searchTerm)
//            } else {
//                getAll()
//            }
//        }
        b.cateHome.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("text", b.txtCateHome.text.toString())
            val fragment = CategorieFragment()
            fragment.arguments = bundle
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_Container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
        b.cateDepa.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("text", b.txtCateDepa.text.toString())
            val fragment = CategorieFragment()
            fragment.arguments = bundle
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_Container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
        b.cateTerre.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("text", b.txtCateTerre.text.toString())
            val fragment = CategorieFragment()
            fragment.arguments = bundle
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_Container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
        b.edtBuscar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val searchTerm = s.toString().trim()
                if (searchTerm.isNotEmpty()) {
                    search(searchTerm)
                } else {
                    // Mostrar todos los registros si el término de búsqueda está vacío
                    adapter.updateList(lstEstate)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        hideKeyboard()
        getAll()

        return view
    }


    private fun getAll() {
        lstEstate = ArrayList()
        adapter = EstateAdapter(lstEstate)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            db.collection("estate")
                .get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        val item = doc.toObject(Estate::class.java)
                        item.idestate = doc.id
                        item.name = doc["name"].toString()
                        item.district = doc["district"].toString()
                        item.city = doc["city"].toString()
//                        item.location = doc["location"].toString()
                        item.price = (doc["price"] as? Double) ?: 0.0
                        item.photo = doc["photo"].toString()

                        item.location = doc["location"] as Map<String, Double>

                        db.collection("users").document(uid)
                            .collection("favorites")
                            .document(item.idestate!!)
                            .get()
                            .addOnSuccessListener { favoriteDoc ->
                                item.isLiked = favoriteDoc.exists()
                                b.listEstates.adapter = adapter
                                b.listEstates.layoutManager = LinearLayoutManager(requireContext())
                                lstEstate.add(item)
                                adapter.updateList(lstEstate)

                            }
                    }
                }
        }
    }


    fun search(searchTerm: String) {
        val filteredList = mutableListOf<Estate>()

        for (estate in lstEstate) {
            if (estate.name?.contains(searchTerm, true) == true ||
                estate.city?.contains(searchTerm, true) == true ||
                estate.district?.contains(searchTerm, true) == true ||
//                estate.location?.contains(searchTerm, true) == true ||
//                estate.location?.latitude == searchTermLatitude && estate.location?.longitude == searchTermLongitude ||
                estate.type?.contains(searchTerm, true) == true
            ) {
                filteredList.add(estate)
            }
        }
        // Actualizar el RecyclerView con la lista filtrada
        adapter.updateList(filteredList)
    }


    private fun hideKeyboard() {
        hideSoftKeyboard(requireContext(), b.root)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            val updatedEstate = data?.getParcelableExtra<Estate>("updated_estate")
            val updatedEstate = data?.getSerializableExtra("updated_estate") as? Estate

            if (updatedEstate != null) {
                // Replace the old estate with the updated one
                val position = lstEstate.indexOfFirst { it.idestate == updatedEstate.idestate }
                lstEstate[position] = updatedEstate

                // Update the list adapter
//                adapter.notifyItemChanged(position)
                adapter.updateList(lstEstate)
            }
        }
    }





}