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
    private const val BASE_URL = "https://dk44n7ll-3000.usw3.devtunnels.ms/"

    //Add this for logging, so we can view it inside the logcat
    private val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }).build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  //retrofit won't know how to convert the http response body into your desired data type, if you don't add a converterFactory, other converter factory (ex.moshi)
            .client(client)
            .build()
    }

}