package com.images.assignment.data

data class UnsplashImageResponse(
    val id: String,
    val description: String?,
    val urls: ImageUrls
)

data class ImageUrls(
    val thumb: String
)