package com.mikali.crudplayground.ui.screens.posts.repository

import com.mikali.crudplayground.R
import com.mikali.crudplayground.network.service.NetworkResult
import com.mikali.crudplayground.network.service.PostApiService
import com.mikali.crudplayground.network.service.RetrofitInstance
import com.mikali.crudplayground.ui.screens.posts.mapper.toPostItem
import com.mikali.crudplayground.ui.screens.posts.mapper.toPostItemResponse
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.screens.posts.model.PostItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class PostRepository(
    private val postApiService: PostApiService = RetrofitInstance.instance.create(PostApiService::class.java),
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) {

    suspend fun getAllPosts(): NetworkResult {
        //Network data flow run on IO thread
        return try {
            withContext(ioDispatcher) {
                val response = postApiService.getAllPosts()
                if (response.isSuccessful) {
                    //convert from network response model to UI model
                    response.body()?.let {
                        NetworkResult.NetworkSuccess(it.map { postItemResponse ->
                            postItemResponse.toPostItem()
                        })
                    } ?: NetworkResult.NetworkSuccess(emptyList<PostItem>())
                    //TODO-handle this with NetworkResult.NetworkEmpty()
                } else { // http status code 300-500
                    NetworkResult.NetworkFailure(R.string.http_request_error)
                }
            }
        } catch (e: UnknownHostException) {
            NetworkResult.NetworkFailure(R.string.no_internet_connection)
        }
    }

    suspend fun createPost(title: String?, body: String?): NetworkResult {
        return try {
            withContext(ioDispatcher) {
                val response = postApiService.createNewPost(
                    postItemResponse = PostItemResponse(
                        id = null,
                        title = title,
                        body = body
                    )
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.NetworkSuccess(it.toPostItem())
                    } ?: NetworkResult.NetworkSuccess(PostItem(title = "", body = ""))

                } else { // http status code 300-500
                    NetworkResult.NetworkFailure(R.string.http_request_error)
                }
            }
        } catch (e: UnknownHostException) {
            NetworkResult.NetworkFailure(R.string.no_internet_connection)
        }
    }

    suspend fun getSinglePost(id: Int): NetworkResult {
        return try {
            withContext(ioDispatcher) {
                val response: Response<PostItemResponse> = postApiService.getSinglePost(id = id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.NetworkSuccess(it.toPostItem())
                    } ?: NetworkResult.NetworkSuccess(PostItem(title = "", body = ""))

                } else { // http status code 300-500
                    NetworkResult.NetworkFailure(R.string.http_request_error)
                }
            }
        } catch (e: UnknownHostException) {
            NetworkResult.NetworkFailure(R.string.no_internet_connection)
        }
    }

    suspend fun updatePost(id: Int, postItem: PostItem): NetworkResult {
        return try {
            withContext(ioDispatcher) {
                val response: Response<PostItemResponse> = postApiService.updateExistingPost(
                    id = id,
                    postItemResponse = postItem.toPostItemResponse()
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        NetworkResult.NetworkSuccess(it.toPostItem())
                    } ?: NetworkResult.NetworkSuccess(PostItem(title = "", body = ""))
                } else { // http status code 300-500
                    NetworkResult.NetworkFailure(R.string.http_request_error)
                }
            }
        } catch (e: UnknownHostException) {
            NetworkResult.NetworkFailure(R.string.no_internet_connection)
        }
    }

    suspend fun deleteSinglePost(id: Int): NetworkResult {
        return try {
            withContext(ioDispatcher) {
                val response = postApiService.deleteSinglePost(id = id)
                if (response.isSuccessful) {
                    NetworkResult.NetworkSuccess(true)
                } else {
                    NetworkResult.NetworkFailure(R.string.http_request_error)
                }
            }
        } catch (e: UnknownHostException) {
            NetworkResult.NetworkFailure(R.string.no_internet_connection)
        }
    }
}