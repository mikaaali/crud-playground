package com.mikali.crudplayground.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikali.crudplayground.ui.CustomBottomAppBar
import com.mikali.crudplayground.ui.common.CardActionDialog
import com.mikali.crudplayground.ui.edit.EditScreen
import com.mikali.crudplayground.ui.image.ImageScreen
import com.mikali.crudplayground.ui.list.ListScreen

@Composable
fun StartNavigation() {
    val navController = rememberNavController()

    //TODO-move to viewModel
    var showListScreenDialog = remember { mutableStateOf(false) }
    var showPhotoScreenDialog = remember { mutableStateOf(false) }

    //TODO-move to viewModel, currently use this to track for icon changes when clicked
    val currentScreen = remember { mutableStateOf(NavigationScreens.LIST) }

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
                        showDialog = showListScreenDialog
                    )
                }
                composable(route = NavigationScreens.EDIT.name) {
                    EditScreen(
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
                    navController = navController
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

