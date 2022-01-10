package com.tuwaiq.halfway.service

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor


object APIClient {
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
}