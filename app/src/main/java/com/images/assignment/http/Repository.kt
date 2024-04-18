package com.images.assignment.http

import com.images.imagessmaple.BuildConfig
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {
    suspend fun getImagesData(page : Int, perPage : Int) = apiService.getImages(BuildConfig.UNSPLASH_ACCESS_KEY,page, perPage )
}