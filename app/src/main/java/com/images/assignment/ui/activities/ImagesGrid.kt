package com.images.assignment.ui.activities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.images.assignment.components.CenteredText
import com.images.assignment.model.MainViewModel
import com.images.assignment.utils.ImageFromBase64String
import com.images.assignment.utils.LoadImageFromUrl

@Composable
fun ImagesGrid(viewModel: MainViewModel) {

    val response = viewModel.imagesState.collectAsLazyPagingItems()

    if(response.itemCount == 0)
    {
        CenteredText(text = "No data found.")
        return
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {

        items(response.itemCount) { i ->
            val item = response[i]
            item?.let {
            /*LoadImageFromUrl(it)*/
                it.imageBlob?.let { it1 -> ImageFromBase64String(it1) }
            }
        }

        response.apply {
            when {
                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {


                    item {
                        //CenteredText("No image found.")
                    }
                }

                loadState.refresh is LoadState.NotLoading -> {
                }
            }
        }
    }
}

