package com.mikali.crudplayground.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikali.crudplayground.ui.CustomBottomAppBar
import com.mikali.crudplayground.ui.common.CardActionDialog
import com.mikali.crudplayground.ui.edit.EditScreen
import com.mikali.crudplayground.ui.image.ImageScreen
import com.mikali.crudplayground.ui.list.ListScreen
import com.mikali.crudplayground.viewmodel.PostSharedViewModel

@Composable
fun StartNavigation() {
    val navController = rememberNavController()

    //TODO-move to viewModel, also note to self MutableState instance is constant, the value inside is mutable, use val
    val showListScreenDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val showPhotoScreenDialog = remember { mutableStateOf(false) }

    //TODO-move to viewModel, currently use this to track for icon changes when clicked, also for the bottom navigation bar
    val currentScreen = remember { mutableStateOf(NavigationScreens.LIST) }

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
                startDestination = NavigationScreens.LIST.name
            ) {
                composable(route = NavigationScreens.LIST.name) {
                    ListScreen(
                        viewModel = postSharedViewModel,
                        showDialog = showListScreenDialog,
                    )
                }
                composable(route = NavigationScreens.EDIT.name) {
                    EditScreen(
                        viewModel = postSharedViewModel,
                        currentScreen = currentScreen,
                        navController = navController,
                    )
                }
                composable(route = NavigationScreens.IMAGE.name) {
                    ImageScreen(
                        showDialog = showPhotoScreenDialog
                    )
                }
            }

            if (showPhotoScreenDialog.value) {
                CardActionDialog(
                    items = listOf("Download", "Some other action"),
                    currentScreen = currentScreen,
                    onDismiss = { showPhotoScreenDialog.value = false },
                    navController = navController,
                )
            }

            // Show Dialog on Card Click
            if (showListScreenDialog.value) {
                CardActionDialog(
                    items = listOf("Edit", "Delete"),
                    currentScreen = currentScreen,
                    onDismiss = { showListScreenDialog.value = false },
                    navController = navController,
                )
            }
        }

        if (currentScreen.value != NavigationScreens.EDIT) {
            CustomBottomAppBar(currentScreen, navController)
        }

    }
}

