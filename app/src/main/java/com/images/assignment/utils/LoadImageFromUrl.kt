package com.images.assignment.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.engage.common.datamodel.Image
import com.images.assignment.data.UnsplashImageResponse
import com.images.assignment.database.ImageEntity
import com.images.imagessmaple.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun LoadImageFromUrl(item: ImageEntity) {
    val url = item.url
    var bitmap by remember(url) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(url) {
        val loadedBitmap = loadImageFromUrl(url)
        bitmap = loadedBitmap
    }

    bitmap?.let {
        Image(bitmap = it.asImageBitmap(), contentDescription = null)
    }
}

@Composable
fun ImageFromBase64String(base64EncodedImageString: String) {
    var bitmap by remember(base64EncodedImageString) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(base64EncodedImageString) {
        val decodedImageBytes = Base64.decode(base64EncodedImageString, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.size)

    }
    if (bitmap == null)
        Placeholder(modifier = Modifier.fillMaxSize())
    else {
        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null)
        }
    }

}

@Composable
fun Placeholder(modifier: Modifier = Modifier) {
    Box(modifier = modifier.wrapContentSize(Alignment.Center)) {
        Image(
            painter = painterResource(R.drawable.no_image),
            contentDescription = null,
            modifier = Modifier.size(48.dp) // Adjust size as needed
        )
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

fun getUrlBytes(url: String): ByteArray? {
    var byteArray: ByteArray? = null
    try {
        val connection = URL(url).openConnection()
        connection.connect()
        val inputStream = BufferedInputStream(connection.getInputStream())
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.flush()
        byteArray = outputStream.toByteArray()
        outputStream.close()
        inputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return byteArray
}


suspend fun getBase64FromImageUrl(url: String): String? {


    return withContext(Dispatchers.IO) {
        try {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()
            require(response.isSuccessful)
            val imgBytes = response.body!!.bytes()
            Base64.encodeToString(imgBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }
}


