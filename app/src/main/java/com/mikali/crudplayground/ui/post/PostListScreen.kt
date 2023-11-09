package com.mikali.crudplayground.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.mikali.crudplayground.R
import com.mikali.crudplayground.navigation.NavigationViewModel
import com.mikali.crudplayground.ui.components.TitleBodyCard
import com.mikali.crudplayground.ui.post.model.PostItem
import com.mikali.crudplayground.ui.post.viewmodel.PostSharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostListScreen(
    postSharedViewModel: PostSharedViewModel,
    navigationViewModel: NavigationViewModel,
) {

    val uiState: State<List<PostItem>> = postSharedViewModel.postListUiState.collectAsState()

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
        ListOfLazyCard(
            postItems = uiState.value,
            onPullRefresh = { postSharedViewModel.fetchAllPosts() },
            onCardClick = {
                navigationViewModel.setShowListScreenDialog(show = true)
                postSharedViewModel.setCurrentSelectSinglePostItem(postItem = it)
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListOfLazyCard(
    postItems: List<PostItem>,
    onPullRefresh: () -> Unit,
    onCardClick: (PostItem) -> Unit,
) {

    val refreshScope = rememberCoroutineScope()
    val refreshing = remember { mutableStateOf(false) }

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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (!refreshing.value) {
                items(items = postItems) { cardItem ->
                    TitleBodyCard(
                        postItem = cardItem,
                        onCardClick = onCardClick
                    )
                }
            }
        }

        PullRefreshIndicator(refreshing.value, refreshState, Modifier.align(Alignment.TopCenter))
    }
}

