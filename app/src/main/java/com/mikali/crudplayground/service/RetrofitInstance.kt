package com.mikali.crudplayground.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Todo- can move this to dependency injection later on
object RetrofitInstance {

    /**
     * Your base url should always end in /, so relative API endpoint can be used in http method calls
     *
     * include base url is ok in the entire http method calls, but too duplicated, not clean
     */
    private const val BASE_URL = "https://kqq8fq6m-3000.usw2.devtunnels.ms/"

    //Add this for logging, so we can view it inside the logcat
    private val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

}