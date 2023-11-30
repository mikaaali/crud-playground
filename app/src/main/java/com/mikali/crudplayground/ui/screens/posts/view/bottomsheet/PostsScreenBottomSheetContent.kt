package com.mikali.crudplayground.ui.screens.posts.view.bottomsheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mikali.crudplayground.ui.main.navigation.ScreenRoutes
import com.mikali.crudplayground.ui.screens.posts.enums.EditMode
import com.mikali.crudplayground.ui.theme.sandYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditAndDeletePostBottomSheetContent(
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    navController: NavHostController,
    onDeleteButtonClicked: () -> Unit,

    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Button to navigate to the edit screen
            Button(
                onClick = {
                    // Coroutine launched to handle the suspend function
                    // This will dismiss the bottom sheet
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                    // Then navigate to the edit screen
                    navController.navigate(ScreenRoutes.EditPost().createRoute(EditMode.EDIT))

                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 2.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    "Edit",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }
            // Button to delete the post
            Button(
                onClick = {
                    // dismiss bottom sheet
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }

                    onDeleteButtonClicked.invoke()
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 2.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = sandYellow)
            ) {
                Text(
                    text = "Delete",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Black
                )
            }
        }
    }
}