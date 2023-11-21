package com.mikali.crudplayground.ui.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mikali.crudplayground.downloadmanager.AppDownloadManagerImpl
import com.mikali.crudplayground.navigation.AppNavHost
import com.mikali.crudplayground.navigation.Screen
import com.mikali.crudplayground.ui.createandedit.CreateAndEditPostViewModel
import com.mikali.crudplayground.ui.photos.view.PhotosScreenBottomSheetContent
import com.mikali.crudplayground.ui.photos.viewmodel.PhotosScreenViewModel
import com.mikali.crudplayground.ui.posts.view.PostsScreenBottomSheetContent
import com.mikali.crudplayground.ui.posts.viewmodel.PostListViewModel
import com.mikali.crudplayground.ui.posts.viewmodel.PostSharedViewModel
import com.mikali.crudplayground.ui.theme.tealGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val appContext = LocalContext.current.applicationContext

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope =
        rememberCoroutineScope() // rememberCoroutineScope is used to launch a coroutine from a composable, move this to viewModel

    val postSharedViewModel: PostSharedViewModel = viewModel()
    val postListViewModel: PostListViewModel = PostListViewModel()
    val createAndEditPostViewModel = CreateAndEditPostViewModel()
    val photosScreenViewModel: PhotosScreenViewModel = viewModel(
        factory = PhotosScreenViewModel.PhotosScreenViewModelFactory(
            appDownloadManager = AppDownloadManagerImpl(context = appContext),
        )
    )

    val downloadRequested: State<Boolean?> =
        photosScreenViewModel.downloadRequested.collectAsState()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val notEditScreen: Boolean = currentRoute != Screen.EditPost().route
    val isPhotoScreen = currentRoute == Screen.Photos.route

    LaunchedEffect(downloadRequested.value) {
        println("hahaha, enter here")
        if (downloadRequested.value == true) {
            bottomSheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetBackgroundColor = tealGreen,
        sheetContent = {

            if (isPhotoScreen) {
                PhotosScreenBottomSheetContent(
                    photosScreenViewModel = photosScreenViewModel,
                )
            } else {
                PostsScreenBottomSheetContent(
                    coroutineScope = coroutineScope,
                    bottomSheetState = bottomSheetState,
                    navController = navController,
                    onDeleteButtonClicked = {
                        postSharedViewModel.onDeleteButtonClick()
                    }
                )
            }

        }
    ) {
        Scaffold(
            topBar = { if (notEditScreen) MainScreenTopBar() },
            bottomBar = { if (notEditScreen) MainScreenBottomBar(navController = navController) },
            content = { paddingValues ->
                AppNavHost(
                    navController = navController,
                    paddingValues = paddingValues,
                    bottomSheetState = bottomSheetState,
                    postSharedViewModel = postSharedViewModel,
                    postListViewModel = postListViewModel,
                    createAndEditPostViewModel = createAndEditPostViewModel,
                    photosScreenViewModel = photosScreenViewModel,
                )
            }
        )
    }

}

