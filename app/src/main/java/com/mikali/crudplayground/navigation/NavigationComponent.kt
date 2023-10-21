package com.mikali.crudplayground.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikali.crudplayground.ui.CustomBottomAppBar
import com.mikali.crudplayground.ui.edit.EditScreen
import com.mikali.crudplayground.ui.image.ImageScreen
import com.mikali.crudplayground.ui.list.CardActionDialog
import com.mikali.crudplayground.ui.list.ListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartNavigation() {
    val navController = rememberNavController()

    //TODO-move to viewModel
    var showDialog = remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { CustomBottomAppBar(navController) }
    ) { _ ->
        // nav host holds all of the composable screens
        NavHost(
            navController = navController,
            startDestination = NavigationScreens.LIST.name
        ) {
            composable(route = NavigationScreens.LIST.name) {
                ListScreen(
                    showDialog = showDialog
                )
            }
            composable(route = NavigationScreens.EDIT.name) {
                EditScreen(
                    navController = navController,
                )
            }
            composable(route = NavigationScreens.IMAGE.name) {
                ImageScreen()
            }
        }
    }

    // Show Dialog on Card Click
    if (showDialog.value) {
        CardActionDialog(onDismiss = { showDialog.value = false }, navController = navController)
    }


}
