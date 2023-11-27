package com.mikali.crudplayground.ui.screens.photos.repository

import com.mikali.crudplayground.R
import com.mikali.crudplayground.network.service.ImageApiService
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.network.service.RetrofitInstance
import com.mikali.crudplayground.ui.screens.photos.mapper.toPhotoItem
import com.mikali.crudplayground.ui.screens.photos.model.PhotoItem
import com.mikali.crudplayground.ui.screens.photos.model.PhotoItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException

class PhotoRepository(
    private val imageApiService: ImageApiService = RetrofitInstance.instance.create(ImageApiService::class.java)
) {

    suspend fun getAllPhotos(): NetworkResult {
        return try {
            withContext(Dispatchers.IO) {
                val response: Response<List<PhotoItemResponse>> = imageApiService.getAllImages()
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.NetworkSuccess(it.map { photoItemResponse ->
                            photoItemResponse.toPhotoItem()
                        })
                    } ?: NetworkResult.NetworkSuccess(emptyList<PhotoItem>())


                } else { // http status code 300-500
                    NetworkResult.NetworkFailure(R.string.http_request_error)
                }
            }
        } catch (e: UnknownHostException) {
            NetworkResult.NetworkFailure(R.string.no_internet_connection)
        }
    }
}