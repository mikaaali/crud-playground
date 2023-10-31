package com.mikali.crudplayground.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.mikali.crudplayground.navigation.NavigationScreens

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarWithBack(
    navController: NavHostController,
    currentScreen: MutableState<NavigationScreens>
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
                currentScreen.value = NavigationScreens.POSTS
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}