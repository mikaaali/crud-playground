package com.mikali.crudplayground.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikali.crudplayground.R
import com.mikali.crudplayground.ui.model.PostItem
import com.mikali.crudplayground.viewmodel.PostSharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListScreen(showDialog: MutableState<Boolean>) {

    val viewModel: PostSharedViewModel = viewModel()
    val uiState: State<List<PostItem>> = viewModel.postListUiState.collectAsState()

    Column(modifier = Modifier.background(Color.LightGray.copy(alpha = 0.3f))) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.crud_playground),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.bodyMedium
        )
        ListScreen(
            postItems = uiState.value,
            showDialog = showDialog,
            onPullRefresh = { viewModel.fetchAllPosts() },
            onCardClick = {
                viewModel.onCardClick(postItem = it)
            }
        )
    }
}

@Composable
fun ListScreen(
    showDialog: MutableState<Boolean>,
    postItems: List<PostItem>,
    onPullRefresh: () -> Unit,
    onCardClick: (PostItem) -> Unit,
) {
    ListOfLazyCard(
        showDialog = showDialog,
        postItems = postItems,
        onPullRefresh = onPullRefresh,
        onCardClick = onCardClick
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListOfLazyCard(
    showDialog: MutableState<Boolean>,
    postItems: List<PostItem>,
    onPullRefresh: () -> Unit,
    onCardClick: (PostItem) -> Unit,
) {

    val refreshScope = rememberCoroutineScope()
    var refreshing = remember { mutableStateOf(false) }


    fun refresh() = refreshScope.launch {
        refreshing.value = true
        delay(1500)
        onPullRefresh.invoke()
        refreshing.value = false
    }

    val refreshState = rememberPullRefreshState(refreshing.value, ::refresh)

    Box(Modifier.pullRefresh(refreshState)) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            if (!refreshing.value) {
                items(items = postItems) { cardItem ->
                    CustomCard(
                        showDialog = showDialog,
                        postItem = cardItem,
                        onCardClick = onCardClick
                    )
                }
            }
        }

        PullRefreshIndicator(refreshing.value, refreshState, Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCard(
    postItem: PostItem,
    showDialog: MutableState<Boolean>,
    onCardClick: (PostItem) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            showDialog.value = true
            onCardClick.invoke(postItem)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = postItem.title.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(text = postItem.body.orEmpty(), style = MaterialTheme.typography.bodySmall)
        }
    }


}