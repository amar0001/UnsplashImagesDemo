package com.images.assignment.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "image_blob") var imageBlob: ByteArray?,
    @ColumnInfo(name = "image_id") var imageID: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEntity

        if (id != other.id) return false
        if (url != other.url) return false
        if (imageBlob != null) {
            if (other.imageBlob == null) return false
            if (!imageBlob.contentEquals(other.imageBlob)) return false
        } else if (other.imageBlob != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (imageBlob?.contentHashCode() ?: 0)
        return result
    }
}