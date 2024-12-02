package com.example.yaksok.feature.place.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlaceNetworkClient {
    private const val BASE_URL = "https://places.googleapis.com/"

    val placesApiService: PlacesApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PlacesApiService::class.java)
    }
}