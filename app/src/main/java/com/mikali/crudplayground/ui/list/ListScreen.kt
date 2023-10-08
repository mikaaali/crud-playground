package com.mikali.crudplayground.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mikali.crudplayground.R
import com.mikali.crudplayground.navigation.NavigationScreens
import com.mikali.crudplayground.ui.theme.Yellow

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ListScreen(navController: NavHostController) {

    //TODO-move to viewModel
    var showDialog = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = "CRUD Playground",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Description",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        },
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(route = NavigationScreens.EDIT.name) },
                shape = RoundedCornerShape(size = 16.dp),
                containerColor = Yellow //TODO-why is dynamic theme not working??
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = {
            ListScreen(showDialog = showDialog, paddingValues = it)
        }
    )

    // Show Dialog on Card Click
    if (showDialog.value) {
        CardActionDialog(onDismiss = { showDialog.value = false }, navController = navController)
    }
}

@Composable
fun ListScreen(showDialog: MutableState<Boolean>, paddingValues: PaddingValues) {
    val data = listOf(
        "title" to "body",
        "title" to "body",
        "title" to "body",
    )
    ListOfLazyCard(showDialog = showDialog, paddingValues = paddingValues, data = data)

}

@Composable
fun ListOfLazyCard(
    showDialog: MutableState<Boolean>,
    paddingValues: PaddingValues,
    data: List<Pair<String, String>>,
) {
    LazyColumn(modifier = Modifier.padding(paddingValues = paddingValues)) {
        items(items = data) { cardItem ->
            CustomCard(
                showDialog = showDialog,
                title = cardItem.first,
                body = cardItem.second,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCard(title: String, body: String, showDialog: MutableState<Boolean>) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(280.dp), // specify the height of your card
        shape = RoundedCornerShape(24.dp),
        onClick = {
            showDialog.value = true
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://example.com/image.jpg")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentDescription = stringResource(R.string.imageDescription),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(2f) // takes 2/3 of the space
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .weight(1f) // takes 1/3 of the space
                    .padding(8.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = body, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}