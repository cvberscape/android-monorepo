package com.example.mapwithmarker.model

import com.google.gson.annotations.SerializedName

data class Location(
	@SerializedName("lat") val lat: Double,
	@SerializedName("lng") val lng: Double
)
