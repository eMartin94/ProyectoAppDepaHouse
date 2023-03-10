package com.example.proyectoappdepahouse

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.adapter.InfoRequestAdapter
import com.example.proyectoappdepahouse.databinding.FragmentInfoBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InfoFragment : Fragment() {

    private lateinit var b: FragmentInfoBinding
    val db = FirebaseFirestore.getInstance()
    private lateinit var lstInfoRequest: ArrayList<Estate>
    private lateinit var adapter: InfoRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentInfoBinding.inflate(inflater, container, false)
        val view = b.root

        val estate = arguments?.getSerializable("estate") as Estate

        b.txtInfoName.text = estate.name.toString().capitalize()
        b.txtInfoUbi.text =
            estate.district.toString().capitalize() + ", " + estate.city.toString().capitalize()
        b.txtInfoPrice.text = "s/ ${String.format("%.2f", estate.price ?: 0.0)}"
        if (estate.photo != null) {
            Glide.with(this).load(estate.photo).into(b.imgInfoEstate)
        }

        b.btnInfoSend.setOnClickListener {

            val edtInfoName = b.edtInfoName.text.toString().trim()
            val edtInfoEmail = b.edtInfoEmail.text.toString().trim()
            val edtInfoPhone = b.edtInfoPhone.text.toString().trim()

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {

                if (edtInfoName.isEmpty() || edtInfoName.length < 3) {
                    Toast.makeText(
                        requireContext(), "Ingrese un nombre v??lido", Toast.LENGTH_SHORT
                    ).show()

                } else if (edtInfoEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(
                        edtInfoEmail
                    ).matches()
                ) {
                    Toast.makeText(
                        requireContext(), "Ingrese un correo v??lido", Toast.LENGTH_SHORT
                    ).show()

                } else if (edtInfoPhone.isEmpty() || edtInfoPhone.length < 9) {
                    Toast.makeText(
                        requireContext(), "Ingrese un n??mero v??lido", Toast.LENGTH_SHORT
                    ).show()

                } else {

                    val userInfoSendRef = db.collection("user_info_send").document(currentUser.uid)
                        .collection("estate").document(estate.idestate!!)
                    userInfoSendRef.get().addOnSuccessListener { documentSnapshot ->
                        val infoSend = documentSnapshot.getBoolean("infoSend") ?: false
                        if (!infoSend) {
                            val infoRequest = hashMapOf(
                                "userId" to currentUser.uid,
                                "name" to estate.name,
                                "district" to estate.district,
                                "city" to estate.city,
                                "price" to estate.price,
                                "photo" to estate.photo,
                            )
                            db.collection("info_request").add(infoRequest).addOnSuccessListener {
                                    userInfoSendRef.set(mapOf("infoSend" to true))
                                        .addOnSuccessListener {

                                            val recipient = "depahouseapp@gmail.com"

                                            sendEmail(recipient, estate)


                                            val fragment = InfoListFragment()
                                            val fragmentManager = requireFragmentManager()
                                            val fragmentTransaction =
                                                fragmentManager.beginTransaction()
                                            fragmentTransaction.replace(
                                                R.id.fragment_Container, fragment
                                            )
                                            fragmentTransaction.commit()

                                            Toast.makeText(
                                                requireContext(),
                                                "Solicitud enviada",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Error al actualizar estado de env??o",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Error al enviar solicitud",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Ya se envi?? una solicitud anteriormente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            requireContext(), "Error al obtener estado de env??o", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        return view
    }


    private fun sendEmail(recipient: String, estate: Estate) {

        val edtInfoName = b.edtInfoName.text.toString().trim()
        val edtInfoEmail = b.edtInfoEmail.text.toString().trim()
        val edtInfoPhone = b.edtInfoPhone.text.toString().trim()

        val subject = "Solicitud de informaci??n del inmueble"
        val body = """
        Informaci??n del inmueble:
            Nombre: ${estate.name}
            Distrito: ${estate.district}
            Ciudad: ${estate.city}
            Precio: ${estate.price}
            Foto: ${estate.photo}

            Enviado por: ${edtInfoName}
            Correo: ${edtInfoEmail}
            N??mero de contacto: ${edtInfoPhone}
        """

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        requireActivity().startActivity(
            Intent.createChooser(
                intent, "Enviar correo"
            )
        )
    }

}