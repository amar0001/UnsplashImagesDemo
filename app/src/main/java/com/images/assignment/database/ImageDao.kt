package com.images.assignment.database

import android.media.Image
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Query("SELECT COUNT(*) FROM images_table")
    suspend fun getImagesCount(): Int

    @Query("SELECT * FROM images_table LIMIT :offset,:limit")
    suspend fun getImages(offset: Int, limit: Int): List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageEntity>)
    @Query("SELECT * FROM images_table WHERE id = :id")
     fun getImageById(id: Long): ImageEntity

    @Query("SELECT image_id FROM images_table")
    suspend fun getAllImageIds(): List<String>



}