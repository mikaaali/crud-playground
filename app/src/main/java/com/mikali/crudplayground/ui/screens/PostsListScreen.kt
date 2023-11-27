package com.mikali.crudplayground.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mikali.crudplayground.ui.main.navigation.Screen
import com.mikali.crudplayground.ui.screens.posts.enums.EditMode
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.screens.posts.view.PostCard
import com.mikali.crudplayground.ui.screens.posts.viewmodel.PostListViewModel
import com.mikali.crudplayground.ui.theme.peach
import com.mikali.crudplayground.ui.theme.tealGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostsListScreen(
        paddingValues: PaddingValues,
        bottomSheetState: ModalBottomSheetState,
        navController: NavController,
        postListViewModel: PostListViewModel
) {
    val postListUiState by postListViewModel.postListUiState.collectAsState(initial = emptyList())

    LaunchedEffect(postListViewModel.events) {
        postListViewModel.events.collect { event ->
            when (event) {
                is PostListViewModel.PostListEvent.OnSuccessDeletePost -> {
                    println("chris onSuccessDeletePost")
                    postListViewModel.fetchAllPosts()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ListOfLazyCard(
                paddingValues = paddingValues,
                postItems = postListUiState,
                onPullRefresh = {
                    postListViewModel.fetchAllPosts()
                },
                onOptionsClick = { postItem ->
                    postListViewModel.setSelectedPostItem(postItem)
                },
                bottomSheetState = bottomSheetState
        )

        FloatingActionButton(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = peach,
                onClick = {
                    navController.navigate(Screen.EditPost().createRoute(EditMode.CREATE))
                },
                modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 60.dp, end = 8.dp) // floating action button is behind the
                        .border(
                                width = 2.dp, // Set the width of the stroke
                                color = Color.Black, // Set the color of the stroke
                                shape = RoundedCornerShape(16.dp) // Apply the same rounded shape to the stroke
                        ),
        ) {
            Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Post",
                    tint = Color.White,
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ListOfLazyCard(
        paddingValues: PaddingValues,
        postItems: List<PostItem>,
        onPullRefresh: () -> Unit,
        onOptionsClick: (PostItem) -> Unit,
        bottomSheetState: ModalBottomSheetState
) {
    val coroutineScope = rememberCoroutineScope() // Create a CoroutineScope tied to this composable
    val isRefreshing = remember { mutableStateOf(false) }

    fun refresh() = coroutineScope.launch {
        isRefreshing.value = true
        delay(1500L)
        onPullRefresh.invoke()
        isRefreshing.value = false
    }

    val refreshState = rememberPullRefreshState(
            refreshing = isRefreshing.value,
            onRefresh = { refresh() }
    )

    Box(
            modifier = Modifier.pullRefresh(refreshState),
    ) {
        LazyColumn(
                Modifier
                        .background(color = tealGreen)
                        .fillMaxSize()
                        .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (!isRefreshing.value) {
                items(items = postItems) { cardItem ->
                    PostCard(
                        postItem = cardItem,
                        onOptionsClick = {
                            onOptionsClick(cardItem) // Make sure you are calling this correctly
                            coroutineScope.launch { // Launch a coroutine
                                showEditAndDeleteBottomSheet(bottomSheetState)
                            }
                        },
                    )
                }
            }
        }

        PullRefreshIndicator(
                refreshing = isRefreshing.value,
                state = refreshState,
                modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
suspend fun showEditAndDeleteBottomSheet(bottomSheetState: ModalBottomSheetState) {
    if (bottomSheetState.isVisible) {
        bottomSheetState.hide()
    } else {
        bottomSheetState.show()
    }
}