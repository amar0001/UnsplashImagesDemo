package com.images.assignment.model

import com.images.assignment.data.UnsplashImageResponse

sealed class UnsplashImageState {
    object Loading:UnsplashImageState()
    data class Success(val list:List<UnsplashImageResponse>):UnsplashImageState()
    data class Error(val message:String):UnsplashImageState()
}