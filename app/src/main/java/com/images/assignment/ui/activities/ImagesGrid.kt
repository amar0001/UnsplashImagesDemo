package com.images.assignment.ui.activities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.images.assignment.model.MainViewModel
import com.images.assignment.model.UnsplashImageState
import com.images.assignment.utils.LoadImageFromUrl


@Composable
fun ImagesGrid(viewModel: MainViewModel) {
    val response = viewModel.imagesState.collectAsLazyPagingItems()

    //val unsplashImageState by viewModel.imagesState.collectAsState()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        modifier = Modifier.fillMaxSize()
    ) {


        items(response.itemCount) { i->
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
                        Text(text = "Error")
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