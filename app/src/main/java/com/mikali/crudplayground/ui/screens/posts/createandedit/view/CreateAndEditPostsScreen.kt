package com.mikali.crudplayground.ui.screens.posts.createandedit.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mikali.crudplayground.ui.alertDialog.CRUDAlertDialog
import com.mikali.crudplayground.ui.alertDialog.CRUDAlertDialogUiState
import com.mikali.crudplayground.ui.components.BasicTextFieldWithPlaceholderText
import com.mikali.crudplayground.ui.screens.posts.createandedit.viewmodel.CreateAndEditPostViewModel
import com.mikali.crudplayground.ui.screens.posts.enums.EditMode
import com.mikali.crudplayground.ui.screens.posts.model.PostItem
import com.mikali.crudplayground.ui.screens.posts.viewmodel.PostListViewModel
import com.mikali.crudplayground.ui.theme.charcoal
import com.mikali.crudplayground.ui.theme.sandYellow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateAndEditPostsScreen(
    editMode: EditMode,
    postListViewModel: PostListViewModel,
    navController: NavHostController,
    createAndEditPostViewModel: CreateAndEditPostViewModel
) {

    val focusManager = LocalFocusManager.current

    val postUiState: State<PostItem> = createAndEditPostViewModel.postItemUiState.collectAsState()

    val alertDialogUiState = remember { mutableStateOf(CRUDAlertDialogUiState(showDialog = false)) }

    // Setup for create/edit mode
    if (editMode == EditMode.CREATE) {
        createAndEditPostViewModel.clearSelectedPostItem()
    } else if (editMode == EditMode.EDIT) {
        createAndEditPostViewModel.setSelectedPostItem(postListViewModel.selectedPostItem.value)
    }

    LaunchedEffect(createAndEditPostViewModel.event) {
        val startTime = System.currentTimeMillis()
        Log.d("haha createAndEditPostViewModel", "LaunchedEffect started collecting at: $startTime")

        createAndEditPostViewModel.event.collectLatest { event ->
            val eventTime = System.currentTimeMillis()
            Log.d("haha createAndEditPostViewModel", "Collected event: $event at: $eventTime")
            when (event) {
                is CreateAndEditPostViewModel.CreateAndEditPostEvent.OnCreatePostSuccess -> {
                    postListViewModel.onPostSuccessfullyCreated()
                    navController.popBackStack()
                    focusManager.clearFocus()
                }

                is CreateAndEditPostViewModel.CreateAndEditPostEvent.OnUpdatePostSuccess -> {
                    navController.popBackStack()
                    focusManager.clearFocus()
                }

                is CreateAndEditPostViewModel.CreateAndEditPostEvent.OnCreatePostFail -> {
                    alertDialogUiState.value = CRUDAlertDialogUiState(
                        showDialog = true,
                        title = "Network Error",
                        text = "Currently unable to create a new post",
                        onConfirm = {
                            alertDialogUiState.value =
                                alertDialogUiState.value.copy(showDialog = false)
                        }
                    )
                }

                is CreateAndEditPostViewModel.CreateAndEditPostEvent.OnUpdatePostFail -> {
                    alertDialogUiState.value = CRUDAlertDialogUiState(
                        showDialog = true,
                        title = "Network Error",
                        text = "Currently unable to update this existing post",
                        onConfirm = {
                            alertDialogUiState.value =
                                alertDialogUiState.value.copy(showDialog = false)
                        }
                    )
                }
            }

            val processingTime = eventTime - startTime
            Log.d(
                "haha createAndEditPostViewModel",
                "Processed event $event in: $processingTime ms"
            )
        }
    }

    if (alertDialogUiState.value.showDialog) {
        CRUDAlertDialog(uiState = alertDialogUiState)
    }

    Scaffold(
        topBar = { CreateAndEditPostScreenTopBar(navController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = charcoal)
                    .padding(bottom = 60.dp)  // To avoid overlapping with the button
            ) {
                //Title Text
                BasicTextFieldWithPlaceholderText(
                    value = postUiState.value.title.orEmpty(),
                    onValueChange = { keyboardInput ->
                        createAndEditPostViewModel.updateTitle(keyboardInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    placeholderText = "Enter title..."
                )

                //Body Text
                BasicTextFieldWithPlaceholderText(
                    value = postUiState.value.body.orEmpty(),
                    onValueChange = { keyboardInput ->
                        createAndEditPostViewModel.updateBody(keyboardInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        color = Color.White,
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    placeholderText = "Enter body..."
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    when (editMode) {
                        EditMode.CREATE -> {
                            createAndEditPostViewModel.createNewPost()
                        }

                        EditMode.EDIT -> {
                            createAndEditPostViewModel.updatePost()
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 2.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = sandYellow)
            ) {
                Text(
                    text = when (editMode) {
                        EditMode.CREATE -> "Create New Post"
                        EditMode.EDIT -> "Update Post"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Upload,
                    contentDescription = "Post",
                    tint = Color.Black
                )
            }
        }
    }
}

