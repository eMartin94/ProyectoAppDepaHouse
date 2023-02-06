package com.example.proyectoappdepahouse.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.EstateDetailsFragment
import com.example.proyectoappdepahouse.R
import com.example.proyectoappdepahouse.model.Estate

class CategorieEstAdapter(private var cates: List<Estate>) :
    RecyclerView.Adapter<CategorieEstAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val description: TextView = itemView.findViewById(R.id.txtCateName)
        val location: TextView = itemView.findViewById(R.id.txtCateUbi)
        val price: TextView = itemView.findViewById(R.id.txtCatePrice)
        val photo: ImageView = itemView.findViewById(R.id.imgCateEstate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_categories, parent, false)
        mContext = parent.context
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val estate = cates[position]
        holder.description.text = estate.name.toString().capitalize()
        holder.location.text = estate.district.toString().capitalize() + ", " + estate.city.toString().capitalize()
        holder.price.text = "s/ ${String.format("%.2f", estate.price ?: 0.0)}"
        if (estate.photo != null) {
            Glide.with(holder.itemView.context)
                .load(estate.photo)
                .into(holder.photo)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("estate", estate)
            val estateDetailsFragment = EstateDetailsFragment()
            estateDetailsFragment.arguments = bundle

            val fragmentManager = (mContext as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_Container, estateDetailsFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun getItemCount(): Int {
        return cates.size
    }
}