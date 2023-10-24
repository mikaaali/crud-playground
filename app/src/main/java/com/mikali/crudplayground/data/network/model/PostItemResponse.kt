package com.mikali.crudplayground.data.network.model

/**
 * The converter we are currently using here is Gson to serialize/deserialize JSON
 * there are other library options: jackson, Gson, Moshi, kotlinx.serialization, etc
 *
 * when dealing with nested JSON and and keys that do not match exactly with kotlin field, we typically need add
 * some annotation (ex. @SerializaedName, part of Gson lib)
 */
data class PostItemResponse(
    val id: Int?,
    val title: String?,
    val body: String?,
)