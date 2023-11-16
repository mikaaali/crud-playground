package com.mikali.crudplayground.service

import com.mikali.crudplayground.data.network.model.ImageItemResponse
import retrofit2.Response
import retrofit2.http.GET

interface ImageApiService {

    //full link: https://dk44n7ll-3000.usw3.devtunnels.ms/images
    @GET("images")
    suspend fun getAllImages(): Response<List<ImageItemResponse>>
}