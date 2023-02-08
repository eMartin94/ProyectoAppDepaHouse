package com.example.proyectoappdepahouse.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.EstateDetailsFragment
import com.example.proyectoappdepahouse.InfoListFragment
import com.example.proyectoappdepahouse.R
import com.example.proyectoappdepahouse.model.Estate
//import com.example.proyectoappdepahouse.model.InfoRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InfoRequestAdapter(private var infoRequests: List<Estate>) :
    RecyclerView.Adapter<InfoRequestAdapter.InfoRequestViewHolder>() {

    private lateinit var db: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var mContext: Context


    class InfoRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val infoName: TextView = itemView.findViewById(R.id.txtInfoReName)
        val infoLocation: TextView = itemView.findViewById(R.id.txtInfoReUbi)
        val infoPrice: TextView = itemView.findViewById(R.id.txtInfoRePrice)
        val infoPhoto: ImageView = itemView.findViewById(R.id.imgInfoRe)
//        val btnInfoSend: ImageView = itemView.findViewById(R.id.btnInfoSend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRequestViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_inforequest, parent, false)
        db = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        mContext = parent.context
        return InfoRequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InfoRequestViewHolder, position: Int) {

        val request = infoRequests[position]
        holder.infoName.text = request.name.toString().capitalize()
        holder.infoLocation.text = request.district.toString().capitalize() + ", " + request.city.toString().capitalize()
        holder.infoPrice.text = "s/ ${String.format("%.2f", request.price ?: 0.0)}"
        if (request.photo != null) {
            Glide.with(holder.itemView.context)
                .load(request.photo)
                .into(holder.infoPhoto)
        }

    }

    override fun getItemCount() = infoRequests.size

    fun updateInfoList(info: ArrayList<Estate>) {
        this.infoRequests = info
        notifyDataSetChanged()
    }
}

