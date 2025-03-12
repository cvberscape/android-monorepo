package com.example.mapwithmarker.model

import com.google.gson.annotations.SerializedName

data class ElevationResponse(
	@SerializedName("results") val results: List<ElevationResult>,
	@SerializedName("status") val status: String
)
