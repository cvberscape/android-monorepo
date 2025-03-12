package com.example.mapwithmarker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapwithmarker.model.ElevationResponse
import com.example.mapwithmarker.model.ElevationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

class MapsMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    private val wDataList = ArrayList<WData>()
    private val elevationResults = ArrayList<ElevationResult>()
    private val latLngList = ArrayList<LatLng>()
    private val markers = ArrayList<Marker>()
    private var heartRateAvg: Double = 0.0

    private val baseUrl = "https://maps.googleapis.com/maps/api/elevation/json?locations="
    private val apiKeyParam = "&key=${BuildConfig.GOOGLE_MAPS_API_KEY}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val db = Firebase.firestore
        val queue = Volley.newRequestQueue(this)

        // Get the current date in "yyyy-MM-dd" format
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        Log.d("MapsMarkerActivity", "Using collection for date: $currentDate")

        db.collection(currentDate)
            .get()
            .addOnSuccessListener { result ->
                result.forEach { document ->
                    document.toObject<WData>()?.let { wDataList.add(it) }
                }
                if (wDataList.isEmpty()) {
                    Log.e("MapsMarkerActivity", "No data retrieved from Firestore")
                    return@addOnSuccessListener
                }

                // Build the location string using joinToString
                val locationString = wDataList.joinToString(separator = "|") { "${it.lat},${it.long}" }

                // Calculate average heart rate
                val heartRateSum = wDataList.sumOf { it.heart }
                heartRateAvg = heartRateSum / wDataList.size

                val fullUrl = baseUrl + locationString + apiKeyParam
                Log.d("MapsMarkerActivity", "Request URL: $fullUrl")
                val request = StringRequest(
                    Request.Method.GET,
                    fullUrl,
                    { response ->
                        try {
                            val elevationResponse = Gson().fromJson(response, ElevationResponse::class.java)
                            elevationResults.addAll(elevationResponse.results)
                            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
                            mapFragment?.getMapAsync(this)
                        } catch (e: Exception) {
                            Log.e("MapsMarkerActivity", "Failed to parse elevation data", e)
                        }
                    },
                    { error ->
                        Log.e("MapsMarkerActivity", "Error in Volley request: ${error.message}")
                    }
                )
                queue.add(request)
            }
            .addOnFailureListener { exception ->
                Log.e("MapsMarkerActivity", "Error getting data from Firestore", exception)
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (wDataList.isEmpty() || elevationResults.isEmpty()) {
            Log.e("MapsMarkerActivity", "Insufficient data to display map")
            return
        }
        // Set camera to the first coordinate
        val firstLocation = LatLng(wDataList.first().lat.toDouble(), wDataList.first().long.toDouble())
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 13f))

        // Add markers and build list of LatLng points
        wDataList.forEachIndexed { index, wData ->
            val latLng = LatLng(wData.lat.toDouble(), wData.long.toDouble())
            latLngList.add(latLng)
            val elevation = elevationResults.getOrNull(index)?.elevation ?: 0.0
            val markerTitle = "Heart: ${wData.heart} - Elevation: ${"%.2f".format(elevation)}"
            googleMap.addMarker(MarkerOptions().position(latLng).title(markerTitle))?.let { markers.add(it) }
        }

        // Initially hide all markers
        markers.forEach { it.isVisible = false }

        // Toggle marker visibility on button click
        findViewById<Button>(R.id.setRangeButton).setOnClickListener {
            markers.forEach { it.isVisible = !it.isVisible }
        }

        // Draw polylines with color coding based on heart rate
        for (i in 0 until latLngList.size - 1) {
            val currentHeart = wDataList[i].heart
            val polylineColor = when {
                currentHeart < heartRateAvg - 1 -> Color.GREEN
                currentHeart in (heartRateAvg - 1)..(heartRateAvg + 3) -> Color.rgb(249, 183, 30)
                currentHeart >= heartRateAvg + 3 -> Color.RED
                else -> Color.BLACK
            }
            googleMap.addPolyline(
                PolylineOptions()
                    .add(latLngList[i], latLngList[i + 1])
                    .width(9f)
                    .color(polylineColor)
            )
        }
    }
}
