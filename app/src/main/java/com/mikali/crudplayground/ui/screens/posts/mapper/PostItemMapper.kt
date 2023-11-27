package com.mikali.crudplayground.ui.screens.posts.mapper

import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.screens.posts.model.PostItemResponse

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
