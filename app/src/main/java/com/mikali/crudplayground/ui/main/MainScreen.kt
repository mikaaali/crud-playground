package com.mikali.crudplayground.ui.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mikali.crudplayground.network.downloadmanager.AppDownloadManagerImpl
import com.mikali.crudplayground.ui.main.navigation.AppNavHost
import com.mikali.crudplayground.ui.main.navigation.ScreenRoutes
import com.mikali.crudplayground.ui.screens.photos.view.bottomsheet.PhotosScreenBottomSheetContent
import com.mikali.crudplayground.ui.screens.photos.viewmodel.PhotosListViewModel
import com.mikali.crudplayground.ui.screens.posts.createandedit.viewmodel.CreateAndEditPostViewModel
import com.mikali.crudplayground.ui.screens.posts.view.bottomsheet.EditAndDeletePostBottomSheetContent
import com.mikali.crudplayground.ui.screens.posts.viewmodel.PostListViewModel
import com.mikali.crudplayground.ui.theme.tealGreen

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val appContext = LocalContext.current.applicationContext

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope =
        rememberCoroutineScope() // rememberCoroutineScope is used to launch a coroutine from a composable, move this to viewModel
    val snackbarHostState = remember { SnackbarHostState() }

    val postListViewModel: PostListViewModel = viewModel()
    val createAndEditPostViewModel: CreateAndEditPostViewModel = viewModel()
    val photosListViewModel: PhotosListViewModel = viewModel(
        factory = PhotosListViewModel.PhotosScreenViewModelFactory(
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
                    photosListViewModel = photosListViewModel,
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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { if (notEditScreenRoutes) MainScreenTopBar() },
            bottomBar = { if (notEditScreenRoutes) MainScreenBottomBar(navController = navController) },
            content = { paddingValues ->
                AppNavHost(
                    navController = navController,
                    paddingValues = paddingValues,
                    bottomSheetState = bottomSheetState,
                    postListViewModel = postListViewModel,
                    createAndEditPostViewModel = createAndEditPostViewModel,
                    photosListViewModel = photosListViewModel,
                    snackbarHostState = snackbarHostState
                )
            }
        )
    }

}

