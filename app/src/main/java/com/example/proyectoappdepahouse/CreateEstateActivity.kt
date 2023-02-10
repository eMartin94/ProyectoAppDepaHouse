package com.example.proyectoappdepahouse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.ActivityCreateEstateBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class CreateEstateActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var b: ActivityCreateEstateBinding

    private var lstEstate = ArrayList<Estate>()
    private lateinit var adapter: EstateAdapter
    private lateinit var storage: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    private var photoUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_estate)
        b = ActivityCreateEstateBinding.inflate(layoutInflater)
        setContentView(b.root)

        storage = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        b.txtBackMain.setOnClickListener {
            var i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        b.btnAdd.setOnClickListener {
            if (photoUri != null) {
                val fileReference = storageReference.child(System.currentTimeMillis().toString())

                fileReference.putFile(photoUri!!)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener { photoUrl ->
                            postEstate(photoUrl.toString())
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@CreateEstateActivity,
                            "La imagen no se pudo cargar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                postEstate()
            }
        }

        b.btnOpenGallery.setOnClickListener {
            selectImage()
        }

    }


    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }

        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            photoUri = data.data!!
            Toast.makeText(
                this@CreateEstateActivity,
                "La imagen se ha cargado correctamente",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun postEstate(photoUrl: String = "") {

        val nameState = b.edtNameEstate.text.toString().trim()
        val city = b.edtCity.text.toString().trim()
        val district = b.edtDistrict.text.toString().trim()
        val latitude = b.edtLat.text.toString().trim()
        val longitude = b.edtLng.text.toString().trim()
        val type = b.edtType.text.toString().trim()
        val price = b.edtPrice.text.toString()

        val dimension = b.edtDimension.text.toString().trim()
        val floor = b.edtFloor.text.toString().trim()
        val room = b.edtRoom.text.toString().trim()
        val badroom = b.edtBadroom.text.toString().trim()
        val kitchen = b.edtKitcen.text.toString().trim()
        val livingroom = b.edtLivingroom.text.toString().trim()
        val pool = b.edtPool.text.toString().trim()

        val db = FirebaseFirestore.getInstance()
        storage = FirebaseFirestore.getInstance()


        val estateMap = hashMapOf(

            "name" to nameState,
            "city" to city,
            "district" to district,
            "location" to hashMapOf(
                "latitude" to latitude.toDouble(),
                "longitude" to longitude.toDouble()
            ),
            "type" to type,
            "price" to price.toDouble(),
            "dimension" to dimension,
            "floor" to floor,
            "room" to room,
            "badroom" to badroom,
            "kitchen" to kitchen,
            "livingroom" to livingroom,
            "pool" to pool,
//            "photo" to "https://images.pexels.com/photos/5088877/pexels-photo-5088877.jpeg",
            "photo" to photoUrl,
            "isLiked" to false
        )

//            db.collection("estate")
//    //            .document(code)
//                .add(estateMap)
//                Toast.makeText(
//                    this@CreateEstateActivity,
//                    "Se ha creado correctamente",
//                    Toast.LENGTH_SHORT
//                ).show()

        db.collection("estate")
            .add(estateMap)
            .addOnSuccessListener {
                Toast.makeText(
                    this@CreateEstateActivity,
                    "Inmueble creado exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@CreateEstateActivity,
                    "Error al crear el inmueble: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }

}