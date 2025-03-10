package com.example.android2023ej.classes

import com.google.gson.annotations.SerializedName


data class Wind (

    @SerializedName("speed" ) var speed : Double? = null,
    @SerializedName("deg"   ) var deg   : Int?    = null,
    @SerializedName("gust"  ) var gust  : Double? = null

)