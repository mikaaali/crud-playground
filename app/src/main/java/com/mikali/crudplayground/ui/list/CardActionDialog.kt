package com.mikali.crudplayground.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mikali.crudplayground.navigation.NavigationScreens
import com.mikali.crudplayground.viewmodel.PostSharedViewModel

@Composable
fun CardActionDialog(onDismiss: () -> Unit, navController: NavHostController) {

    val viewModel: PostSharedViewModel = viewModel()

    // Grey box fill the entire screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            //semi-transparent black
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
    ) {
        // Bottom section
        Column(
            modifier = Modifier
                // placed at the bottom of the screen
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            // Edit/Delete action
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(Color.White)
            ) {
                val items = listOf("Edit", "Delete")
                items.forEachIndexed { index, item ->
                    Text(
                        text = item,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // handle item click
                                if (item == "Edit") {
                                     viewModel.onEditButtonClick()
                                    navController.navigate(route = NavigationScreens.EDIT.name)
                                } else {
                                    viewModel.onDeleteButtonClick()
                                }
                                onDismiss()
                            }
                            .padding(8.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally) // centers the text
                    )

                    // Adding a divider between "Edit" and "Delete"
                    if (index < items.size - 1) {
                        Divider(color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }

            // Add space between the Edit/Delete actions and cancel button
            Spacer(modifier = Modifier.height(16.dp))

            // Button Below
            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(24.dp)) // Making corners rounded to match
                    .background(Color.White), // setting the background color of the button
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black // adjusting the color of the text
                ),
            ) {
                Text("Cancel", style = MaterialTheme.typography.headlineSmall)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}