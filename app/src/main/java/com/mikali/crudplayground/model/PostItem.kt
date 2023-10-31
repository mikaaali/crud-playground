package com.mikali.crudplayground.model

/**
 * There is already a converter that's configured in retrofit builder that allow you to parse JSON
 * object with the exact matching key
 *
 * when dealing with nested JSON and and keys that do not match exactly with kotlin field, we need
 * to use a thrid party serialization library, ex jackson, Gson, Moshi, kotlinx.serialization, etc.
 */
data class PostItem(
    val id: Int,
    val title: String,
    val body: String,
    val image_url: String,
)