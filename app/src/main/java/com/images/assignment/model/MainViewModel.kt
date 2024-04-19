package com.images.assignment.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.images.assignment.data.UnsplashImageResponse
import com.images.assignment.database.ImageEntity
import com.images.assignment.database.ImagesRepository
import com.images.assignment.http.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val imagesRepository: ImagesRepository,
    private val application: Application
) : ViewModel() {

    private val _imagesState: MutableStateFlow<PagingData<ImageEntity>> =
        MutableStateFlow(PagingData.empty())
    var imagesState = _imagesState.asStateFlow()
        private set

    private val _errorState: MutableLiveData<Throwable?> = MutableLiveData()
    val errorState: MutableLiveData<Throwable?> = _errorState

    init {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    20, enablePlaceholders = true
                )
            ) {
                UnsplashPagingSource(repository, imagesRepository, application)

            }.flow.cachedIn(viewModelScope).onEach {
                    newData ->
                _imagesState.value = newData
                _errorState.postValue(null)
            }.onCompletion {
                if (_errorState.value == null) {
                    // Handle error by displaying an error message or retrying the network call
                    _errorState.postValue(IllegalStateException("Failed to fetch images"))
                }
            }.collect {
                _imagesState.value = it
            }
        }
    }

}
