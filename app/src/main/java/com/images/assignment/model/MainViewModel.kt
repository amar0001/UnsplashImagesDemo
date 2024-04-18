package com.images.assignment.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.images.assignment.data.UnsplashImageResponse
import com.images.assignment.database.ImagesRepository
import com.images.assignment.http.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val unsplashPagingSource: UnsplashPagingSource,
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val _imagesState: MutableStateFlow<PagingData<UnsplashImageResponse>> =
        MutableStateFlow(PagingData.empty())
    var imagesState = _imagesState.asStateFlow()
        private set

    init {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    30, enablePlaceholders = true
                )
            ) {
                unsplashPagingSource
            }.flow.cachedIn(viewModelScope).collect {
                _imagesState.value = it
            }
        }
    }

}
