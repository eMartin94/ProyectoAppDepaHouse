package com.example.proyectoappdepahouse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.FragmentUpdateEstateBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class UpdateEstateFragment : Fragment() {

    private lateinit var b: FragmentUpdateEstateBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var estateId: String? = null
    private lateinit var estate: Estate
    private lateinit var adapter: EstateAdapter
    private val db = FirebaseFirestore.getInstance()

    private var photoUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var spinnerType: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentUpdateEstateBinding.inflate(inflater, container, false)
        val view = b.root


        estateId = requireArguments().getString("estate_id")

        spinnerType = b.spinnerType
        val typesArray = resources.getStringArray(R.array.types)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, typesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        if (estateId != null) {
            getEstate(estateId!!)
        } else {
            Toast.makeText(requireContext(), "Error: Inmueble no encontrado", Toast.LENGTH_SHORT)
                .show()
        }

        b.btnOpenGallery.setOnClickListener {
            openGallery()
        }

        b.progressBar.visibility = View.INVISIBLE
        b.btnSave.setOnClickListener {
            updateEstate()

        }

        b.txtBackMain.setOnClickListener {
            var i = Intent(requireContext(), MainActivity::class.java)
            startActivity(i)

        }

        return view
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            photoUri = data.data
            b.imgEstate.setImageURI(photoUri)
        }
    }

    private fun getEstate(id: String) {
        db.collection("estate")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    estate = document.toObject(Estate::class.java)!!
//                    listHomeFragment = supportFragmentManager.findFragmentById(R.id.fragment_Container) as? ListHomeFragment

                    populateFields()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: Inmueble no encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "Error al obtener el inmueble: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun populateFields() {

        b.edtNameEstate.setText(estate.name)
        b.edtCity.setText(estate.city)
        b.edtDistrict.setText(estate.district)
        b.edtLat.setText(estate.location?.get("latitude").toString())
        b.edtLng.setText(estate.location?.get("longitude").toString())
        b.edtPrice.setText(estate.price.toString())
        b.edtDimension.setText(estate.dimension.toString())
        b.edtFloor.setText(estate.floor).toString()
        b.edtRoom.setText(estate.room).toString()
        b.edtBadroom.setText(estate.badroom).toString()
        b.edtKitcen.setText(estate.kitchen).toString()
        b.edtLivingroom.setText(estate.livingroom).toString()
        b.edtPool.setText(estate.pool).toString()

        val typesArray = resources.getStringArray(R.array.types)
        val typeIndex = typesArray.indexOf(estate.type)
        b.spinnerType.setSelection(typeIndex)

        if (estate.photo != null) {
            Glide.with(this)
                .load(estate.photo)
                .placeholder(R.drawable.ic_profile)
                .into(b.imgEstate)
        } else {
            b.imgEstate.setImageResource(R.drawable.ic_profile)
        }

    }

    private fun updateEstate() {

        val nameState = b.edtNameEstate.text.toString().trim()
        val city = b.edtCity.text.toString().trim()
        val district = b.edtDistrict.text.toString().trim()
        val latitude = b.edtLat.text.toString().trim()
        val longitude = b.edtLng.text.toString().trim()
        val type = spinnerType.selectedItem.toString().trim()
        val price = b.edtPrice.text.toString().trim()

        val dimension = b.edtDimension.text.toString().trim()
        val floor = b.edtFloor.text.toString().trim()
        val room = b.edtRoom.text.toString().trim()
        val badroom = b.edtBadroom.text.toString().trim()
        val kitchen = b.edtKitcen.text.toString().trim()
        val livingroom = b.edtLivingroom.text.toString().trim()
        val pool = b.edtPool.text.toString().trim()

        if (nameState.isEmpty() || city.isEmpty() || district.isEmpty() || price == null) {
            Toast.makeText(
                requireContext(),
                "Debe completar todos los campos obligatorios",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val newEstate = Estate(
//            idestate = estate.idestate,
            name = nameState,
            city = city,
            district = district,
            location = hashMapOf(
                "latitude" to latitude.toDouble(),
                "longitude" to longitude.toDouble()
            ),
            type = type,
            price = price.toDouble(),
            photo = estate.photo,
            dimension = dimension,
            floor = floor,
            room = room,
            badroom = badroom,
            livingroom = livingroom,
            kitchen = kitchen,
            pool = pool,
        )

        if (photoUri != null) {
            uploadPhoto(newEstate)
        } else {
            updateFirestore(newEstate, estateId!!)
        }

    }

    private fun uploadPhoto(newEstate: Estate) {

        b.progressBar.visibility = View.VISIBLE

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    newEstate.photo = it.toString()
                    updateFirestore(newEstate, estateId!!)
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Error al subir imagen: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateFirestore(newEstate: Estate, id: String) {
        db.collection("estate")
            .document(id)
            .set(newEstate)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Inmueble actualizado", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "Error al actualizar el inmueble: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}