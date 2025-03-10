package com.example.android2023ej.classes

import com.google.gson.annotations.SerializedName

data class FeedbackSend (

    @SerializedName("id"         ) var id         : Int?        = null,
    @SerializedName("data" ) var data : Data? = Data(),

    )