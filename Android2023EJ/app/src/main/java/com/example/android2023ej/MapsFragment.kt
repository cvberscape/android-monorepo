package com.example.android2023ej

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.android2023ej.classes.weatherData
import com.example.android2023ej.databinding.FragmentMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->

        // Draw marker on rovaniemi
        val rovaniemi = LatLng(66.503059, 25.726967)
        googleMap.addMarker(MarkerOptions().position(rovaniemi).title("Rovaniemi"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(rovaniemi))

        // Checkbox listener, if ticked shows zoom controls
        val checkb = binding.checkBox
        checkb.setOnCheckedChangeListener { _, isChecked ->
            googleMap.uiSettings.isZoomControlsEnabled = isChecked
        }

        // Listener for 3 radio buttons, changes the map type depending on selection
        binding.radioNormal.setOnClickListener {
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        binding.radioHybrid.setOnClickListener {
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
        binding.radioTerrain.setOnClickListener {
            googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }

        // Marker listener, on click runs fetchJsonFromUrl
        googleMap.setOnMarkerClickListener { marker ->
            fetchJsonFromUrl()
            true // return true to indicate that the event has been handled
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun fetchJsonFromUrl() {
        // Define OpenWeatherMap API KEY
        val API_KEY : String = BuildConfig.OWM_KEY
        // Define URL for GET request
        val JSON_URL = "https://api.openweathermap.org/data/2.5/weather?lat=60.192059&lon=24.945831&appid=${API_KEY}&units=metric"

        // Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // Print raw response to log
                Log.d("ADVTECH", response)

                // Parse JSON response to objects using gson
                val gson = GsonBuilder().setPrettyPrinting().create()
                var item  = gson.fromJson(response, weatherData::class.java)
                val itemwind = item.wind?.speed.toString()
                val itemtemp = item.main?.temp.toString()
                val itemhumidity = item.main?.humidity.toString()

                // Launches MapsInfoFragment and passes wind,temp and humidity values
                val action = MapsFragmentDirections.actionMapsFragmentToMapsInfoFragment(itemwind,itemtemp,itemhumidity)
                findNavController().navigate(action)

            },

            // Logs for errors
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
            }) {
        }


        // Add the request to the RequestQueue.
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}

