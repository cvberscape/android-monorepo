package com.example.mapwithmarker.model

import com.google.gson.annotations.SerializedName

data class ElevationResult(
	@SerializedName("elevation") val elevation: Double,
	@SerializedName("location") val location: Location,
	@SerializedName("resolution") val resolution: Double
)
