package com.images.assignment.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.images.assignment.data.UnsplashImageResponse
import com.images.assignment.database.ImagesRepository
import com.images.assignment.http.Repository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UnsplashPagingSource @Inject constructor(
    private val repository: Repository,
    private val imagesRepository: ImagesRepository
) : PagingSource<Int, UnsplashImageResponse>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashImageResponse>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImageResponse> {
        val page = params.key ?: 1
        // Check if data exists in the database
       // val dataExistsInDb = imagesRepository.isDataInDatabase()


        val response = repository.getImagesData(page, params.loadSize)
        return try {
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.isEmpty()) null else page.plus(1)
            )
        } catch (e: IOException) {
            LoadResult.Error(
                e
            )
        } catch (e: HttpException) {
            LoadResult.Error(
                e
            )
        }
    }
}