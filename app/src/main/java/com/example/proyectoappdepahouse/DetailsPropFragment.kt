package com.example.proyectoappdepahouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectoappdepahouse.databinding.FragmentDetailsPropBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView

class DetailsPropFragment : Fragment() {

    private lateinit var b: FragmentDetailsPropBinding
    private lateinit var mMapView: MapView
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentDetailsPropBinding.inflate(inflater, container, false)
        val view = b.root



        return view
    }

}