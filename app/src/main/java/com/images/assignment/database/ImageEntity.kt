package com.images.assignment.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
data class ImageEntity(@PrimaryKey val id: Long, val url: String, var imageBlob: ByteArray?)