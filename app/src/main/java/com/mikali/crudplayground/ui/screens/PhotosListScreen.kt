package com.mikali.crudplayground.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mikali.crudplayground.R
import com.mikali.crudplayground.data.network.model.ImageItemResponse
import com.mikali.crudplayground.ui.photos.viewmodel.PhotosScreenViewModel
import com.mikali.crudplayground.ui.theme.tealGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhotosListScreen(
    paddingValues: PaddingValues,
    bottomSheetState: ModalBottomSheetState,
    photosScreenViewModel: PhotosScreenViewModel,
) {

    val uiState: State<List<ImageItemResponse>> = photosScreenViewModel.images.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Number of columns
        contentPadding = paddingValues, // Padding around the grid
        modifier = Modifier.background(tealGreen)
    ) {
        items(uiState.value) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = stringResource(R.string.imageDescription),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clickable {
                        coroutineScope.launch {
                            photosScreenViewModel.onPhotoClicked(imageUrl = it.imageUrl)
                            showBottomSheet(bottomSheetState)
                        }
                    }
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                //TODO: handle loading, success, errror, etc. async image have built in listener

            )
        }
    }
}