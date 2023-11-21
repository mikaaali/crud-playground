package com.mikali.crudplayground.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.mikali.crudplayground.ui.screens.posts.viewmodel.PostSharedViewModel

@Composable
fun CardInteractionDialog(
    items: List<String>,
    onDismiss: () -> Unit,
) {

    val postSharedViewModel: PostSharedViewModel = viewModel()
    val singlePostUiState = postSharedViewModel.singlePostUiState.collectAsState()

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
                items.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // handle item click
                                when (item) {
                                    "Edit" -> {

                                    }

                                    "Delete" -> {
                                        onDismiss()
                                    }
                                }
                            }
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.CenterHorizontally) // centers the text
                                .align(Alignment.Center)
                        )
                    }

                    // Adding a divider between "Edit" and "Delete"
                    if (index < items.size - 1) {
                        Divider(color = Color.Gray)
                    }
                }

            }

            // Add space between the Edit/Delete actions and cancel button
            Spacer(modifier = Modifier.height(16.dp))

            // Cancel Below
            Button(
                onClick = { onDismiss.invoke() },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black, // adjusting the color of the text
                ),
                contentPadding = PaddingValues(16.dp) // remove default padding
            ) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}