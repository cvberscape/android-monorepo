package com.example.android2023ej.classes
import com.google.gson.annotations.SerializedName

data class weatherData  (

    @SerializedName("main"       ) var main       : Main?              = Main(),
    @SerializedName("wind"       ) var wind       : Wind?              = Wind(),


)