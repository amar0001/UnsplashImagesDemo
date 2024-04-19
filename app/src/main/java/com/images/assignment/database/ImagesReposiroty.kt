package com.images.assignment.database

import com.images.assignment.MyApplication
import com.images.assignment.data.UnsplashImageResponse
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val imageDao: ImageDao
) {




    suspend fun getImagesFromDatabase(page: Int, pageSize: Int): List<ImageEntity> {
        val offset = (page - 1) * pageSize
        return imageDao.getImages(offset, pageSize)
    }

    suspend fun insertImages(images: List<ImageEntity>) {
        imageDao.insertAll(images)
    }

    suspend fun getStoredImageIds(): List<String> {
        // Retrieve all image IDs from the database
        return imageDao.getAllImageIds()
    }

    suspend fun checkImagesInDatabase(images: List<UnsplashImageResponse>): List<UnsplashImageResponse> {
        // Get IDs of stored images
        val storedImageIds = getStoredImageIds()

        // Filter fetched images to find those not in the database
        return images.filter {
            it.id !in storedImageIds
        }
    }
}

