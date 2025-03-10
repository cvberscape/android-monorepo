package com.example.android2023ej.classes

import com.google.gson.annotations.SerializedName


data class Attributes (

    @SerializedName("name"      ) var name      : String? = null,
    @SerializedName("location"  ) var location  : String? = null,
    @SerializedName("value"     ) var value     : String? = null,
    @SerializedName("createdAt" ) var createdAt : String? = null,
    @SerializedName("updatedAt" ) var updatedAt : String? = null

)