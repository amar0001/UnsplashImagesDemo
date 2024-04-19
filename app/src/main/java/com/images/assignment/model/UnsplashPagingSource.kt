package com.images.assignment.model

import android.app.Application
import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.images.assignment.data.UnsplashImageResponse
import com.images.assignment.database.ImageEntity
import com.images.assignment.database.ImagesRepository
import com.images.assignment.http.Repository
import com.images.assignment.utils.NetworkChecker
import com.images.assignment.utils.getBase64FromImageUrl
import com.images.assignment.utils.getUrlBytes
import com.images.assignment.utils.isInternetAvailable
import com.images.assignment.utils.showToast
import com.images.assignment.utils.toast
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class UnsplashPagingSource @Inject constructor(
    private val repository: Repository,
    private val imagesRepository: ImagesRepository,
    private val application: Application,
) : PagingSource<Int, ImageEntity>() {

    override fun getRefreshKey(state: PagingState<Int, ImageEntity>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageEntity> {
        val page = params.key ?: 1

        val resultList = getData(page, params)

        try {

            if (resultList.isNotEmpty()) {
                // Return the fetched data
                return LoadResult.Page(
                    data = resultList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (resultList.isEmpty()) null else page + 1
                )
            } else {
                // If the result list is empty, return error
                return LoadResult.Error(Exception())
            }
        } catch (e: IOException) {
            // Return IO error
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // Return HTTP error
            return LoadResult.Error(e)
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun getData(
        page: Int,
        params: PagingSource.LoadParams<Int>
    ): List<ImageEntity> {

        if (isInternetAvailable(application)) {
            val result = repository.getImagesData(page, params.loadSize)
            val notInDb = imagesRepository.checkImagesInDatabase(result)
            if (notInDb.isNotEmpty()) {

                val response = convertToImageEntities(notInDb)
                // Insert fetched data into the database
                imagesRepository.insertImages(response)
            }
        } else {
            GlobalScope.launch {
                showToast(application ,"Network is not available, loading images from database")

            }


        }

        return imagesRepository.getImagesFromDatabase(page, params.loadSize)
    }

    private suspend fun convertToImageEntities(responseList: List<UnsplashImageResponse>): List<ImageEntity> {
        return responseList.map { response ->
            ImageEntity(
                imageID = response.id,
                url = response.urls.regular,
                imageBlob = getBase64FromImageUrl(response.urls.regular)
            )
        }
    }
}
