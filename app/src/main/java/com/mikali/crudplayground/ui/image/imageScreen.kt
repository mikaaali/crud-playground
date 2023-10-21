package com.mikali.crudplayground.ui.image

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mikali.crudplayground.R
import com.mikali.crudplayground.data.network.model.ImageItem
import com.mikali.crudplayground.viewmodel.ImageScreenViewModel

@Composable
fun ImageScreen() {

    val imageScreenViewModel: ImageScreenViewModel = viewModel()
    val uiState: State<List<ImageItem>> = imageScreenViewModel.images.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Number of columns
        contentPadding = PaddingValues(16.dp) // Padding around the grid
    ) {
        items(uiState.value) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it.image_url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = stringResource(R.string.imageDescription),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
        }
    }
}