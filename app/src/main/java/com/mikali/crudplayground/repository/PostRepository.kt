package com.mikali.crudplayground.repository

import com.mikali.crudplayground.R
import com.mikali.crudplayground.data.network.model.PostItemResponse
import com.mikali.crudplayground.service.NetworkResult
import com.mikali.crudplayground.service.PostApiService
import com.mikali.crudplayground.service.RetrofitInstance
import com.mikali.crudplayground.ui.posts.mapper.toPostItem
import com.mikali.crudplayground.ui.posts.mapper.toPostItemResponse
import com.mikali.crudplayground.ui.posts.model.PostItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.UnknownHostException

class PostRepository(
    private val postApiService: PostApiService = RetrofitInstance.instance.create(PostApiService::class.java)
) {

    suspend fun getAllPosts(): NetworkResult {
        //Network data flow run on IO thread
        return try {
            withContext(Dispatchers.IO) {
                val response = postApiService.getAllPosts()
                if (response.isSuccessful) {
                    //convert from network response model to UI model
                    response.body()?.let {
                        NetworkResult.NetworkSuccess(it.map { postItemResponse ->
                            postItemResponse.toPostItem()
                        })
                    }
                        ?: NetworkResult.NetworkSuccess(emptyList<PostItem>()) //TODO-handle this with NetworkResult.NetworkEmpty()

                } else { // http status code 300-500
                    NetworkResult.NetworkFailure(R.string.http_request_error)
                }
            }
        } catch (e: UnknownHostException) {
            NetworkResult.NetworkFailure(R.string.no_internet_connection)
        }
    }

    //Asynchronous execution using enqueue and callbacks.
    /*    fun getAllPosts(callback: (List<PostItem>?, Throwable?) -> Unit) {
            val call = postService.getAllPosts()

            call.enqueue(object : Callback<List<PostItem>> {
                override fun onResponse(call: Call<List<PostItem>>, response: Response<List<PostItem>>) {
                    if (response.isSuccessful) {
                        callback(response.body(), null)
                    } else {
                        callback(null, IOException("Error fetching posts, response: ${response.errorBody()}"))
                    }
                }

                override fun onFailure(call: Call<List<PostItem>>, t: Throwable) {
                    callback(null, t)
                }
            })
        }*/

    suspend fun createPost(title: String?, body: String?): NetworkResult {
        return try {
            withContext(Dispatchers.IO) {
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
            withContext(Dispatchers.IO) {
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
            withContext(Dispatchers.IO) {
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
            withContext(Dispatchers.IO) {
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