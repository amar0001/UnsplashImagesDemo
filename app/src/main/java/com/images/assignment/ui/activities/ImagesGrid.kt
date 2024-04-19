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
import com.images.assignment.utils.LoadImageFromUrl

@Composable
fun ImagesGrid(viewModel: MainViewModel) {

    val response = viewModel.imagesState.collectAsLazyPagingItems()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {

        items(response.itemCount) { i ->
            val item = response[i]
            item?.let { LoadImageFromUrl(it) }
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
                        CenteredText("No image found.")
                    }
                }

                loadState.refresh is LoadState.NotLoading -> {
                }
            }
        }
    }


//    when (unsplashImageState) {
//        is UnsplashImageState.Error ->
//        {
//            Text(
//                text = "Failed to images data.",
//                fontSize = 18.sp,
//                color = Color.Red
//            )
//        }
//        is UnsplashImageState.Loading -> {
//            CircularProgressIndicator()
//        }
//        is UnsplashImageState.Success -> {
//            val images = (unsplashImageState as UnsplashImageState.Success).list
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                modifier = Modifier
//                    .padding(1.dp)
//                    .fillMaxWidth()
//            ) {
//                items(images.size) { i ->
//                    val item = images[i]
//                    LoadImageFromUrl(item)
//                }
//            }
//        }
//    }


}

