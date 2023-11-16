package com.mikali.crudplayground.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * The converter we are currently using here is Gson to serialize/deserialize JSON
 * there are other library options: jackson, Gson, Moshi, kotlinx.serialization, etc
 *
 * when dealing with nested JSON and and keys that do not match exactly with kotlin field, we typically need add
 * some annotation (ex. @SerializedName, part of Gson lib)
 */
data class PostItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("body")
    val body: String?,
)