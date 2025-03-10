package com.example.android2023ej.classes

import com.google.gson.annotations.SerializedName

data class todoDataClass (

    @SerializedName("userId"    ) var userId    : Int?     = null,
    @SerializedName("id"        ) var id        : Int?     = null,
    @SerializedName("title"     ) var title     : String?  = null,
    @SerializedName("completed" ) var completed : Boolean? = null

)