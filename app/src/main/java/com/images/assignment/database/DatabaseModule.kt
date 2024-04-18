package com.images.assignment.database

import android.content.Context
import androidx.room.Room
import com.images.assignment.database.AppDatabase
import com.images.assignment.database.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "images_db"
        ).build()
    }

    @Provides
    fun provideImageDao(appDatabase: AppDatabase): ImageDao {
        return appDatabase.imageDao()
    }
}