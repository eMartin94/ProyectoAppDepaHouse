package com.example.proyectoappdepahouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.ActivityCreateEstateBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateEstateActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var b: ActivityCreateEstateBinding

    private var lstEstate = ArrayList<Estate>()
    private lateinit var adapter: EstateAdapter
    private lateinit var storage: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_estate)
        b = ActivityCreateEstateBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.txtBackMain.setOnClickListener {
            var i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        b.btnAdd.setOnClickListener {
            postEstate()
        }
    }

    private fun postEstate() {

        val nameState = b.edtNameEstate.text.toString().trim()
        val city = b.edtCity.text.toString().trim()
        val district = b.edtDistrict.text.toString().trim()
        val location = b.edtLocation.text.toString().trim()
        val type = b.edtType.text.toString().trim()
        val price = b.edtPrice.text.toString()

        val db = FirebaseFirestore.getInstance()
        storage = FirebaseFirestore.getInstance()


        val estateMap = hashMapOf(

            "name" to nameState,
            "city" to city,
            "district" to district,
            "location" to location,
            "type" to type,
            "price" to price.toDouble(),
            "photo" to "https://images.pexels.com/photos/5088877/pexels-photo-5088877.jpeg",
            "isLiked" to false
        )


        db.collection("estate")
//            .document(code)
            .add(estateMap)
            Toast.makeText(
                this@CreateEstateActivity,
                "Se ha creado correctamente",
                Toast.LENGTH_SHORT
            ).show()

//        val estateMap: MutableMap<String, Any> = HashMap()
//        estateMap["name"] = nameState
//        estateMap["city"] = city
//        estateMap["district"] = district
//        estateMap["location"] = location
//        estateMap["type"] = type
//        estateMap["price"] = price
//        estateMap["photo"] = "https://images.pexels.com/photos/5088877/pexels-photo-5088877.jpeg"

//            .addOnSuccessListener {
//                db.collection("estate")
//                    .addSnapshotListener { querySnapshot, e ->
//                        if (e != null) {
//                            Toast.makeText(
//                                this@CreateEstateActivity,
//                                "Ocurrió un error",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        lstEstate.clear()
//                        for (doc in querySnapshot!!) {
//                            val alumno = doc.toObject(Estate::class.java)
//                            lstEstate.add(alumno)
//                        }
//                        Toast.makeText(
//                            this@CreateEstateActivity,
//                            "Se ha creado correctamente",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        adapter.notifyDataSetChanged()
//                    }
//            }


    }

}