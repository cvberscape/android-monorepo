package com.example.android2023ej.classes

import com.google.gson.annotations.SerializedName

data class Feedback (

    @SerializedName("id"         ) var id         : Int?        = null,
    @SerializedName("attributes" ) var attributes : Attributes? = Attributes(),

)