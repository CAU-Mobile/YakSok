package com.example.yaksok.feature.route.data.remote

import com.example.yaksok.feature.route.data.model.DirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {
    //세부사항 없이
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
        @Query("key") apiKey: String = "API_KEY_REPLACE"
//        @Query("key") apiKey: String = com.google.maps.android.ktx.utils.BuildConfig.deb
    ): DirectionsResponse


    //도착시간
    @GET("directions/json")
    suspend fun getDirectionsArr(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("arrival_time") arrivalTime: Int,
        @Query("transit_mode") transitMode: String = "",
        @Query("transit_routing_preference") transitRoutingPreference: String = "",
        @Query("mode") mode: String = "transit",
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
        @Query("key") apiKey: String = "API_KEY_REPLACE"
    ): DirectionsResponse

    //출발시간
    @GET("directions/json")
    suspend fun getDirectionsDep(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("departure_time") departureTime: Int,
        @Query("transit_mode") transitMode: String = "",
        @Query("transit_routing_preference") transitRoutingPreference: String = "",
        @Query("mode") mode: String = "transit",
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
        @Query("key") apiKey: String = "API_KEY_REPLACE"
    ): DirectionsResponse


    //시간 없이
    @GET("directions/json")
    suspend fun getDirectionsTmRp(
        @Query("origin") origin: String = "london bridge",
        @Query("destination") destination: String = "granada",
        @Query("transit_mode") transitMode: String = "",
        @Query("transit_routing_preference") transitRoutingPreference: String = "",
        @Query("mode") mode: String = "transit",
        @Query("alternatives") alternatives: Boolean = true,
        @Query("language") language: String = "ko",
        @Query("key") apiKey: String = "API_KEY_REPLACE"
    ): DirectionsResponse

}