package com.example.proyectoappdepahouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectoappdepahouse.databinding.FragmentEstatesBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EstatesFragment : Fragment() {

    private val SELECT_IMAGE_REQUEST_CODE = 100
    private lateinit var b: FragmentEstatesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentEstatesBinding.inflate(inflater, container, false)

        b.selectImageButton.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_PICK)
            imageIntent.type = "image/*"
            startActivityForResult(imageIntent, SELECT_IMAGE_REQUEST_CODE)
        }

        return b.root
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val selectedImage = data?.data
//            if (selectedImage != null) {
//                val estate = Estate(
//                    name = b.nameEstateEditText.text.toString(),
//                    price = b.priceEditText.text.toString(),
//                    district = b.disctricEditText.text.toString(),
//                    city = b.cityEditText.text.toString(),
//                    location = b.locationEditText.text.toString(),
//                    photo = b.selectImageButton.text.toString()
//                )
//
//                val storageRef = FirebaseStorage.getInstance().reference.child("estate_images/${UUID.randomUUID()}")
//                storageRef.putFile(selectedImage)
//                    .addOnSuccessListener { taskSnapshot ->
//                        storageRef.downloadUrl.addOnSuccessListener { imageUrl ->
//                            val data = hashMapOf(
//                                "nameEstate" to estate.name,
//                                "price" to estate.price,
//                                "disctric" to estate.district,
//                                "city" to estate.city,
//                                "location" to estate.location,
//                                "photo" to imageUrl.toString()
//                            )
//
//                            FirebaseFirestore.getInstance().collection("estates")
//                                .add(data)
//                                .addOnSuccessListener { onSuccess() }
//                                .addOnFailureListener { e -> onFailure(e) }
//                        }
//                    }
//                    .addOnFailureListener { e -> onFailure(e) }
//            }
//        }
//    }

    private fun onSuccess() {
        // Handle success
    }

    private fun onFailure(e: Exception) {
        // Handle failure
    }




}