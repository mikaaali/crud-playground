package com.mikali.crudplayground.ui.posts.model

/**
 * Better practice to have separate model for network response vs UI state,
 * Because many API response will give you super large set of data, but your UI may not need all of them
 * There is always the option to not deserialize all the data from network, but it's better practice
 * to deserialize/capture all the network response, and then create separate UI model to handle app needed data
 */
data class PostItem(
    val id: Int? = null,
    val title: String?,
    val body: String?,
) {
    companion object {
        val Empty = PostItem(id = null, title = null, body = null)
    }
}
