package com.example.proyectoappdepahouse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.R
import com.example.proyectoappdepahouse.model.Estate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EstateAdapter(private var estates: List<Estate>) :
    RecyclerView.Adapter<EstateAdapter.EstateViewHolder>() {

    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String

    fun updateList(newList: List<Estate>) {
        estates = newList
        notifyDataSetChanged()
    }

    fun updateFavorites(favorites: ArrayList<Estate>) {
        this.estates = favorites
        notifyDataSetChanged()
    }

    class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val description: TextView = itemView.findViewById(R.id.txtDescrip)
        val location: TextView = itemView.findViewById(R.id.txtUbi)
        val price: TextView = itemView.findViewById(R.id.txtPrice)
        val photo: ImageView = itemView.findViewById(R.id.imgEstate)
        val btnLiked: ImageView = itemView.findViewById(R.id.btnLiked)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_estate, parent, false)
        db = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        return EstateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {


        val estate = estates[position]
        holder.description.text = estate.name.toString().capitalize()
        holder.location.text = estate.district.toString().capitalize() + ", " + estate.city.toString().capitalize()
        holder.price.text = "s/ ${String.format("%.2f", estate.price ?: 0.0)}"
        if (estate.photo != null) {
            Glide.with(holder.itemView.context)
                .load(estate.photo)
                .into(holder.photo)
        }

        val item = estates[position]

        if (item.isLiked) {
            holder.btnLiked.setImageResource(R.drawable.ic_is_liked)
        } else {
            holder.btnLiked.setImageResource(R.drawable.ic_no_liked)
        }

        holder.btnLiked.setOnClickListener {
            val estateId = item.idestate

            if (item.isLiked) {
                item.isLiked = false
                holder.btnLiked.setImageResource(R.drawable.ic_no_liked)
                // Eliminar de la lista de favoritos del usuario conectado
                db.collection("users").document(userId).collection("favorites")
                    .document(item.idestate!!)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Eliminado de Favoritos", Toast.LENGTH_SHORT).show()
                    }
                notifyDataSetChanged()
            } else {
                item.isLiked = true
                holder.btnLiked.setImageResource(R.drawable.ic_is_liked)
                // Agregar a la lista de favoritos del usuario conectado
                val newFav = hashMapOf(
                    "name" to item.name,
                    "city" to item.city,
                    "district" to item.district,
                    "location" to item.location,
                    "type" to item.type,
                    "price" to item.price,
                    "photo" to item.photo,
                    "idestate" to item.idestate
                )
                db.collection("users").document(userId).collection("favorites")
                    .document(estateId!!)
                    .set(newFav)
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Agregado a Favoritos", Toast.LENGTH_SHORT).show()
                    }
                notifyDataSetChanged()
            }

        }


    }

    override fun getItemCount(): Int {

        return estates.size
    }

//        fun catpitalizaText(originalText: String): String {
//            return originalText.capitalize()
//        }


}