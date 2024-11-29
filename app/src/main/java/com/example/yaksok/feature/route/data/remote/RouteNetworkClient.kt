package com.example.yaksok.feature.route.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RouteNetworkClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val directionsApiService: DirectionsApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DirectionsApiService::class.java)
    }
}