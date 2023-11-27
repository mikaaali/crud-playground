package com.mikali.crudplayground.ui.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mikali.crudplayground.network.downloadmanager.AppDownloadManagerImpl
import com.mikali.crudplayground.ui.main.navigation.AppNavHost
import com.mikali.crudplayground.ui.main.navigation.ScreenRoutes
import com.mikali.crudplayground.ui.screens.photos.view.PhotosScreenBottomSheetContent
import com.mikali.crudplayground.ui.screens.photos.viewmodel.PhotosScreenViewModel
import com.mikali.crudplayground.ui.screens.posts.createandedit.CreateAndEditPostViewModel
import com.mikali.crudplayground.ui.screens.posts.view.EditAndDeletePostBottomSheetContent
import com.mikali.crudplayground.ui.screens.posts.viewmodel.PostListViewModel
import com.mikali.crudplayground.ui.theme.tealGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val appContext = LocalContext.current.applicationContext

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope =
        rememberCoroutineScope() // rememberCoroutineScope is used to launch a coroutine from a composable, move this to viewModel

    val postListViewModel: PostListViewModel = viewModel()
    val createAndEditPostViewModel: CreateAndEditPostViewModel = viewModel()
    val photosScreenViewModel: PhotosScreenViewModel = viewModel(
        factory = PhotosScreenViewModel.PhotosScreenViewModelFactory(
            appDownloadManager = AppDownloadManagerImpl(context = appContext),
        )
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val notEditScreenRoutes: Boolean = currentRoute != ScreenRoutes.EditPost().route
    val isPhotoScreenRoutes = currentRoute == ScreenRoutes.Photos.route


    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetBackgroundColor = tealGreen,
        sheetContent = {
            if (isPhotoScreenRoutes) {
                PhotosScreenBottomSheetContent(
                    coroutineScope = coroutineScope,
                    bottomSheetState = bottomSheetState,
                    photosScreenViewModel = photosScreenViewModel,
                )
            } else {
                EditAndDeletePostBottomSheetContent(
                    coroutineScope = coroutineScope,
                    bottomSheetState = bottomSheetState,
                    navController = navController,
                    onDeleteButtonClicked = {
                        postListViewModel.onDeleteButtonClick()
                    }
                )
            }

        }
    ) {
        Scaffold(
            topBar = { if (notEditScreenRoutes) MainScreenTopBar() },
            bottomBar = { if (notEditScreenRoutes) MainScreenBottomBar(navController = navController) },
            content = { paddingValues ->
                AppNavHost(
                    navController = navController,
                    paddingValues = paddingValues,
                    bottomSheetState = bottomSheetState,
                    postListViewModel = postListViewModel,
                    createAndEditPostViewModel = createAndEditPostViewModel,
                    photosScreenViewModel = photosScreenViewModel,
                )
            }
        )
    }

}

