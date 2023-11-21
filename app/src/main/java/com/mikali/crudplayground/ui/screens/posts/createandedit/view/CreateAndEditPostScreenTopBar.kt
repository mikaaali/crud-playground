package com.mikali.crudplayground.ui.screens.posts.createandedit.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mikali.crudplayground.ui.theme.charcoal
import com.mikali.crudplayground.ui.theme.sandYellow

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreateAndEditPostScreenTopBar(
    navController: NavHostController,
    //currentScreen: MutableState<NavigationScreens>
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Doodle Dairy ", color = sandYellow, fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Mo - Jan 30",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
                //currentScreen.value = NavigationScreens.POSTS
            }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = charcoal)
        )
}