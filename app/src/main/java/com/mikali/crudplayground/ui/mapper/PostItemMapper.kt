package com.mikali.crudplayground.ui.mapper

import com.mikali.crudplayground.data.network.model.PostItemResponse
import com.mikali.crudplayground.ui.model.PostItem

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
