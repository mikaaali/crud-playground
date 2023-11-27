package com.mikali.crudplayground.ui.screens.photos.mapper

import com.mikali.crudplayground.ui.screens.photos.model.PhotoItem
import com.mikali.crudplayground.ui.screens.photos.model.PhotoItemResponse

fun PhotoItemResponse.toPhotoItem(): PhotoItem =
    PhotoItem(
        id = this.id,
        imageUrl = this.imageUrl,
    )

fun PhotoItem.toPhotoItemResponse(): PhotoItemResponse =
    PhotoItemResponse(
        id = this.id,
        imageUrl = this.imageUrl,
    )
