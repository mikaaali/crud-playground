package com.mikali.crudplayground.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikali.crudplayground.ui.CustomBottomAppBar
import com.mikali.crudplayground.ui.components.CardInteractionDialog
import com.mikali.crudplayground.ui.photos.PhotoListScreen
import com.mikali.crudplayground.ui.post.PostEditScreen
import com.mikali.crudplayground.ui.post.PostListScreen
import com.mikali.crudplayground.ui.post.viewmodel.PostSharedViewModel

@Composable
fun StartNavigation() {
    val navController = rememberNavController()

    val navigationViewModel: NavigationViewModel = viewModel()
    val showListScreenDialog = navigationViewModel.showPostScreenActionDialog.collectAsState()
    val editMode = navigationViewModel.editMode.collectAsState()

    val showPhotoScreenDialog = remember { mutableStateOf(false) }

    //TODO-move to viewModel, currently use this to track for icon changes when clicked, also for the bottom navigation bar
    val currentScreen = remember { mutableStateOf(NavigationScreens.POSTS) }

    /**
     * TODO: I had to do this because ListScreen and EditScreen's viewModel.hashCode() are different
     *
     * Not sure what the best way to fix this, so lifted those two viewModel from being inside each view
     * to just a single variable on a higher level, better practice to have each screen contain
     * it's own viewModel, instead of passing the same variable around,
     * maybe related to viewModel scoped to NavBackStack,
     * don't have enough knowledge, look into this later
     */
    val postSharedViewModel: PostSharedViewModel = viewModel()

    Column(
        Modifier.fillMaxSize()
    ) {
        Box(
            // take up all the remaining space after consider the other child first (the other child is bottom app bar)
            modifier = Modifier.weight(1f)
        ) {
            // nav host holds all of the composable screens
            NavHost(
                navController = navController,
                startDestination = NavigationScreens.POSTS.name
            ) {
                composable(route = NavigationScreens.POSTS.name) {
                    PostListScreen(
                        postSharedViewModel = postSharedViewModel,
                        navigationViewModel = navigationViewModel,
                    )
                }
                composable(route = NavigationScreens.EDIT.name) {
                    PostEditScreen(
                        editMode = editMode.value,
                        postSharedViewModel = postSharedViewModel,
                        currentScreen = currentScreen,
                        navController = navController,
                    )
                }
                composable(route = NavigationScreens.PHOTOS.name) {
                    PhotoListScreen(
                        showDialog = showPhotoScreenDialog
                    )
                }
            }

            if (showPhotoScreenDialog.value) {
                CardInteractionDialog(
                    items = listOf("Download", "Some other action"),
                    onEditClick = {},
                    currentScreen = currentScreen,
                    onDismiss = { showPhotoScreenDialog.value = false },
                    navController = navController,
                )
            }

            // Show Dialog on Card Click
            if (showListScreenDialog.value) {
                CardInteractionDialog(
                    items = listOf("Edit", "Delete"),
                    onEditClick = { navigationViewModel.setEditMode(mode = EditMode.EDIT) },
                    currentScreen = currentScreen,
                    onDismiss = { navigationViewModel.setShowListScreenDialog(show = false) },
                    navController = navController,
                )
            }
        }

        if (currentScreen.value != NavigationScreens.EDIT) {
            CustomBottomAppBar(
                currentScreen = currentScreen,
                navController = navController,
                onCreateClick = { navigationViewModel.setEditMode(EditMode.CREATE) }
            )
        }

    }
}

