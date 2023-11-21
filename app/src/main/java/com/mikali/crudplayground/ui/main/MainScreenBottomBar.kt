package com.mikali.crudplayground.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mikali.crudplayground.ui.main.navigation.Screen
import com.mikali.crudplayground.ui.common.ChipIcon
import com.mikali.crudplayground.ui.theme.peach
import com.mikali.crudplayground.ui.theme.tealGreen

@Composable
fun MainScreenBottomBar(
    navController: NavController,
) {
    BottomNavigation(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ),
        backgroundColor = tealGreen,
        elevation = 16.dp,

        ) {
        val currentDestination = navController.currentDestination?.route
        BottomNavigationItem(
            icon = {
                val selected = currentDestination == Screen.Posts.route
                ChipIcon(
                    icon = if (selected) Icons.Filled.PostAdd else Icons.Outlined.PostAdd,
                    selected = selected,
                    enabledColor = peach
                )
            },
            label = {
                Text(
                    "Posts",
                    color = if (currentDestination == Screen.Posts.route) peach else LocalContentColor.current.copy(
                        alpha = ContentAlpha.medium
                    ),
                    fontSize = 10.sp
                )
            },
            selected = currentDestination == Screen.Posts.route,
            onClick = {
                navController.navigate(route = Screen.Posts.route)
            }
        )
        BottomNavigationItem(
            icon = {
                val selected = currentDestination == Screen.Photos.route
                ChipIcon(
                    icon = if (selected) Icons.Filled.Photo else Icons.Outlined.Photo,
                    selected = selected,
                    enabledColor = peach
                )
            },
            label = {
                Text(
                    "Photos",
                    color = if (currentDestination == Screen.Photos.route) peach else LocalContentColor.current.copy(
                        alpha = ContentAlpha.medium
                    ),
                    fontSize = 10.sp // Smaller text size
                )
            },
            selected = currentDestination == Screen.Photos.route,
            onClick = {
                navController.navigate(route = Screen.Photos.route)
            }
        )
    }
}


