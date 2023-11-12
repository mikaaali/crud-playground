package com.mikali.crudplayground.ui.posts.mapper

import com.mikali.crudplayground.data.network.model.PostItemResponse
import com.mikali.crudplayground.ui.posts.model.PostItem

fun PostItemResponse.toPostItem(): PostItem =
    PostItem(
        id = this.id,
        title = this.title,
        body = this.body,
    )

fun PostItem.toPostItemResponse(): PostItemResponse =
    PostItemResponse(
        id = this.id,
        title = this.title,
        body = this.body,
    )
