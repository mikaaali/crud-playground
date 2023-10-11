package com.mikali.crudplayground.repository

import com.mikali.crudplayground.model.PostItem
import com.mikali.crudplayground.service.PostService
import com.mikali.crudplayground.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository(
    private val postService: PostService = RetrofitInstance.instance.create(PostService::class.java)
) {

    suspend fun getAllPosts(): List<PostItem> {
        //Network data flow run on IO thread
        return withContext(Dispatchers.IO) {
            val response = postService.getAllPosts()
            if (response.isSuccessful) {
                response.body()!!
            } else {
                emptyList()
            }
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
}