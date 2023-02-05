package com.example.proyectoappdepahouse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.R
import com.example.proyectoappdepahouse.model.Estate

class EstateAdapter(private val estates: List<Estate>) :
    RecyclerView.Adapter<EstateAdapter.EstateViewHolder>() {

    class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val description: TextView = itemView.findViewById(R.id.txtDescrip)
        val location: TextView = itemView.findViewById(R.id.txtUbi)
        val price: TextView = itemView.findViewById(R.id.txtPrice)
        val photo: ImageView = itemView.findViewById(R.id.imgEstate)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_estate, parent, false)
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

    }

    override fun getItemCount(): Int {

        return estates.size
    }

//        fun catpitalizaText(originalText: String): String {
//            return originalText.capitalize()
//        }


}