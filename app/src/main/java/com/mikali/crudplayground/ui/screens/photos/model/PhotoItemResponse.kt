package com.mikali.crudplayground.ui.screens.photos.model

import com.google.gson.annotations.SerializedName

data class PhotoItemResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
)
