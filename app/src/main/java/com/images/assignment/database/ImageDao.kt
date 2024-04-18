package com.images.assignment.database

import android.media.Image
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAllImages(vararg images: ImageEntity)

    @Query("SELECT * FROM images_table WHERE id = :id")
     fun getImageById(id: Long): ImageEntity



}