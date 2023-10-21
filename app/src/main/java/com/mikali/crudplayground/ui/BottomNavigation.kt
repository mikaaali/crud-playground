package com.mikali.crudplayground.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.mikali.crudplayground.navigation.NavigationScreens
import com.mikali.crudplayground.ui.theme.Yellow

@Composable
fun CustomBottomAppBar(navController: NavController) {
    BottomAppBar {
        val currentDestination = navController.currentBackStackEntry?.destination?.route
        NavigationScreens.values().forEach {
            IconButton(
                onClick = {
                    navController.navigate(route = it.name) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            ) {
                val icon = when (it) {
                    NavigationScreens.LIST -> Icons.Default.List
                    NavigationScreens.EDIT -> Icons.Default.Add
                    NavigationScreens.IMAGE -> Icons.Default.Face
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (currentDestination == it.name) Yellow else Color.Gray
                )
            }
        }
    }
}