package com.mikali.crudplayground.data.network.model

import com.google.gson.annotations.SerializedName

data class ImageItemResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
)
