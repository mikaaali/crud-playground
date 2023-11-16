package com.mikali.crudplayground.repository

import com.mikali.crudplayground.data.network.model.ImageItemResponse
import com.mikali.crudplayground.service.ImageApiService
import com.mikali.crudplayground.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepository(
    private val imageApiService: ImageApiService = RetrofitInstance.instance.create(ImageApiService::class.java)
) {

    suspend fun getAllImages(): List<ImageItemResponse> {
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