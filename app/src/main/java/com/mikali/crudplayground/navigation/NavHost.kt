package com.mikali.crudplayground.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mikali.crudplayground.ui.photos.view.PhotosScreen
import com.mikali.crudplayground.ui.posts.editview.EditPostScreen
import com.mikali.crudplayground.ui.posts.enums.EditMode
import com.mikali.crudplayground.ui.posts.listview.PostsScreen
import com.mikali.crudplayground.ui.posts.viewmodel.PostSharedViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    bottomSheetState: ModalBottomSheetState,
    postSharedViewModel: PostSharedViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Posts.route
    ) {
        composable(route = Screen.Posts.route) {
            PostsScreen(
                paddingValues = paddingValues,
                bottomSheetState = bottomSheetState,
                postSharedViewModel = postSharedViewModel,
                navController = navController,
            )
        }
        composable(
            route = Screen.EditPost().route,
            arguments = listOf(navArgument("editMode") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val editMode: EditMode = navBackStackEntry.arguments?.getString("editMode")?.let {
                EditMode.valueOf(it)
            } ?: EditMode.EDIT
            EditPostScreen(
                editMode = editMode,
                postSharedViewModel = postSharedViewModel,
                navController = navController,
            )
        }
        composable(route = Screen.Photos.route) {
            PhotosScreen(
                paddingValues = paddingValues,
                bottomSheetState = bottomSheetState,
            )
        }
    }
}