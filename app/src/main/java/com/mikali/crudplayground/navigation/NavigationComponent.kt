package com.mikali.crudplayground.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikali.crudplayground.ui.edit.EditScreen
import com.mikali.crudplayground.ui.list.ListScreen

@Composable
fun StartNavigation() {
    val navController = rememberNavController()

    // nav host holds all of the composable screens
    NavHost(
        navController = navController,
        startDestination = NavigationScreens.LIST.name
    ) {
        composable(route = NavigationScreens.LIST.name) {
            ListScreen(navController = navController)
        }
        composable(route = NavigationScreens.EDIT.name) {
            EditScreen(
                navController = navController
            )
        }
    }
}
