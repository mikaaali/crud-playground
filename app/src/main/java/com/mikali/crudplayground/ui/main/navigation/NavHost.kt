package com.mikali.crudplayground.ui.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mikali.crudplayground.ui.screens.posts.createandedit.CreateAndEditPostViewModel
import com.mikali.crudplayground.ui.screens.photos.viewmodel.PhotosScreenViewModel
import com.mikali.crudplayground.ui.screens.posts.enums.EditMode
import com.mikali.crudplayground.ui.screens.posts.viewmodel.PostListViewModel
import com.mikali.crudplayground.ui.screens.posts.viewmodel.PostSharedViewModel
import com.mikali.crudplayground.ui.screens.CreateAndEditPostsScreen
import com.mikali.crudplayground.ui.screens.PhotosListScreen
import com.mikali.crudplayground.ui.screens.PostsListScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    bottomSheetState: ModalBottomSheetState,
    postSharedViewModel: PostSharedViewModel,
    postListViewModel: PostListViewModel,
    createAndEditPostViewModel: CreateAndEditPostViewModel,
    photosScreenViewModel: PhotosScreenViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Posts.route
    ) {
        composable(route = Screen.Posts.route) {
            PostsListScreen(
                paddingValues = paddingValues,
                bottomSheetState = bottomSheetState,
                navController = navController,
                postListViewModel = postListViewModel,
            )
        }
        composable(
            route = Screen.EditPost().route,
            arguments = listOf(navArgument("editMode") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val editMode: EditMode = navBackStackEntry.arguments?.getString("editMode")?.let {
                EditMode.valueOf(it)
            } ?: EditMode.EDIT
            CreateAndEditPostsScreen(
                editMode = editMode,
                postSharedViewModel = postSharedViewModel,
                navController = navController,
                createAndEditPostViewModel = createAndEditPostViewModel
            )
        }
        composable(route = Screen.Photos.route) {
            PhotosListScreen(
                paddingValues = paddingValues,
                bottomSheetState = bottomSheetState,
                photosScreenViewModel = photosScreenViewModel,
            )
        }
    }
}