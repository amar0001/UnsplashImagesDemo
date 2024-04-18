package com.images.assignment.http

import com.images.assignment.data.UnsplashImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
   suspend fun getImages(@Query("client_id") clientId: String, @Query("page") page: Int, @Query("per_page") perPage: Int): List<UnsplashImageResponse>
}