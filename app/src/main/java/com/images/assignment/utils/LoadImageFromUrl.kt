package com.images.assignment.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import com.images.assignment.data.UnsplashImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun LoadImageFromUrl(item: UnsplashImageResponse) {
    val url = item.urls
    var bitmap by remember(url) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(url) {
        val loadedBitmap = loadImageFromUrl(url.regular)
        bitmap = loadedBitmap
    }

    bitmap?.let {
        Image(bitmap = it.asImageBitmap(), contentDescription = null)
    }
}

suspend fun loadImageFromUrl(url: String): Bitmap? {
    var bitmap: Bitmap? = null
    withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return bitmap
}
