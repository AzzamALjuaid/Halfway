package com.tuwaiq.halfway.service

import com.tuwaiq.halfway.model.NearByLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapAPI {

    @GET("place/nearbysearch/json")
    fun getNearBy(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String?,
        @Query("key") key: String?
    ): Call<NearByLocation?>?
}
