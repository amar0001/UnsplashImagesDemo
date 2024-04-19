package com.images.assignment.database

import android.media.Image
import androidx.room.Database
import androidx.room.RoomDatabase

//V. imp : https://developer.android.com/training/data-storage/room/migrating-db-versions
@Database(entities = [ImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}