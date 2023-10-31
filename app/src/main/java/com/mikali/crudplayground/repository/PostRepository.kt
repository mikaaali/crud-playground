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
}