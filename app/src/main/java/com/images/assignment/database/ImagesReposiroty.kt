package com.images.assignment.database

import com.images.assignment.MyApplication
import javax.inject.Inject

class ImagesRepository @Inject constructor(

    private val imageDao: ImageDao
) {

     fun saveImageToDb(image: ImageEntity) {
       imageDao.insertAllImages(image)
    }

     fun saveImagesToDb(images: List<ImageEntity>) {
       imageDao.insertAllImages(*images.toTypedArray())
    }

     fun getImageFromDb(id: Long): ImageEntity {
        return imageDao.getImageById(id)
    }
}

