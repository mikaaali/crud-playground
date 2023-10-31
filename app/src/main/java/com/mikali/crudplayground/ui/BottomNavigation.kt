package com.mikali.crudplayground.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mikali.crudplayground.navigation.NavigationScreens
import com.mikali.crudplayground.ui.theme.Yellow

@Composable
fun CustomBottomAppBar(
    currentScreen: MutableState<NavigationScreens>,
    navController: NavController,
) {
    BottomAppBar {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationScreens.values().forEach { screen ->
                Box(modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        navController.navigate(route = screen.name) {
                            currentScreen.value = screen

                            if (screen == NavigationScreens.EDIT) {
                                //clear current stack have just list screen, since that's what we set as the start destination
                                popUpTo(navController.graph.startDestinationId)
                            }

                            //don't add another instance of the screen to the stack, when we are already on this screen
                            launchSingleTop = true
                        }
                    }
                )) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (screen == NavigationScreens.EDIT) {
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate(route = screen.name) {
                                        currentScreen.value = screen

                                        if (screen == NavigationScreens.EDIT) {
                                            popUpTo(navController.graph.startDestinationId)
                                        }

                                        launchSingleTop = true
                                    }
                                },
                                containerColor = Yellow,
                                contentColor = Color.Black
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                            }
                        } else {
                            val iconSize = 30.dp
                            val icon = when (screen) {
                                NavigationScreens.POSTS -> if (currentScreen.value == screen) Icons.Filled.Summarize else Icons.Outlined.Summarize
                                NavigationScreens.PHOTOS -> if (currentScreen.value == screen) Icons.Filled.PhotoLibrary else Icons.Outlined.PhotoLibrary
                                else -> null
                            }

                            icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null,
                                    modifier = Modifier.size(iconSize)
                                )
                            }

                            // Add text only for LIST and IMAGE
                            when (screen) {
                                NavigationScreens.POSTS -> Text("Posts")
                                NavigationScreens.PHOTOS -> Text("Photos")
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}
