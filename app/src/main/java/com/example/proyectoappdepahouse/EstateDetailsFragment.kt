package com.example.proyectoappdepahouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.proyectoappdepahouse.adapter.EstateAdapter
import com.example.proyectoappdepahouse.databinding.FragmentEstateDetailsBinding
import com.example.proyectoappdepahouse.model.Estate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class EstateDetailsFragment : Fragment(), OnMapReadyCallback {

    val db = FirebaseFirestore.getInstance()
    private lateinit var b: FragmentEstateDetailsBinding
    private lateinit var lstEstate: ArrayList<Estate>
    private lateinit var adapter: EstateAdapter

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
        b = FragmentEstateDetailsBinding.inflate(inflater, container, false)
        val view = b.root

        val estate = arguments?.getSerializable("estate") as Estate

        b.txtDetailsDescription.text = estate.name
        b.txtDetailsLocation.text = estate.district + ", " + estate.city
        b.txtDetailsPrice.text = "s/ ${String.format("%.2f", estate.price ?: 0.0)}"
        b.txtDetailsDimension.text = estate.dimension.toString() + "m²"
        b.txtDetailsFloor.text = estate.floor
        b.txtDetailsRooms.text = estate.room
        b.txtDetailsBadrooms.text = estate.badroom
        b.txtDetailsLivingrooms.text = estate.livingroom
        b.txtDetailsKitchen.text = estate.kitchen
        b.txtDetailsPool.text = estate.pool
//        b.txtDetailsLatlng.text = "${estate.location?.get("latitude")}, ${estate.location?.get("longitude")}"
        if (estate.photo != null) {
            Glide.with(this)
                .load(estate.photo)
                .into(b.imgDetailsPhoto)
        }

        mMapView = b.mapView
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val estate = arguments?.getSerializable("estate") as Estate
        this.googleMap = googleMap

        val location = estate.location
        val nameLocation = estate.district
        if (location != null) {
            val lat = location["latitude"] ?: 0.0
            val long = location["longitude"] ?: 0.0
            val latLng = LatLng(lat, long)
    //        val location = LatLng(-12.09812, -77.03566)
//            googleMap.addMarker(MarkerOptions().position(location).title("San Francisco"))
            googleMap.addMarker(MarkerOptions().position(latLng).title(nameLocation))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        }
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

}