package com.mikali.crudplayground.ui.screens.photos.repository

import com.mikali.crudplayground.network.service.ImageApiService
import com.mikali.crudplayground.network.service.RetrofitInstance
import com.mikali.crudplayground.ui.screens.photos.model.ImageItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(
    private val imageApiService: ImageApiService = RetrofitInstance.instance.create(ImageApiService::class.java)
) {

    suspend fun getAllPhotos(): List<ImageItemResponse> {
        return withContext(Dispatchers.IO) {
            val response = imageApiService.getAllImages()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                emptyList<ImageItemResponse>()
            }
        }

    }
}