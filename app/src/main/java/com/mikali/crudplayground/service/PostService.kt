package com.mikali.crudplayground.service

import com.mikali.crudplayground.model.PostItem
import retrofit2.Response
import retrofit2.http.GET

interface PostService {

    //full url: https://kqq8fq6m-3000.usw2.devtunnels.ms/posts
    /**
     * Important this need to be suspend because even if it's not suspend it will run on the I/O thread
     * because it's inside a coroutine scope(ex.withContext(Dispatchers.IO), but it block the I/O thread,
     * meaning the thread is occupied with the function call until it completes. completes as no other tasks
     * can use this thread until this function rerun an result or throws an exception.
     *
     * Suspend means while waiting for the result, it release the thread back to the thread pool.
     * Once you get the result, it will take over the I/O thread again.
     *
     * When you use a suspending function for a network API request, you send the request and while waiting
     * for the response, the I/O thread can be free to do other stuff
     *
     * So always make API service function suspend
     *
     * this is just using retrofit annotations to describe http api calls in kotlin
     */
    @GET("posts")
    suspend fun getAllPosts(): Response<List<PostItem>> //Utilizing Kotlin coroutines.

    /**
     * Asynchronous execution using enqueue and callbacks. We have the dependency from
     * from retrofit2 to get Call return type
     */
    /*    @GET("posts")
        fun getAllPosts(): Call<List<PostItem>>*/

    /**
     * Using Reactive Programming (RxJava). Need to add RxJava dependency
     */
    /*    @GET("posts")
        fun getAllPosts(): Observable<List<PostItem>>*/

}