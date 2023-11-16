package com.mikali.crudplayground.ui.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mikali.crudplayground.ui.common.BasicTextFieldWithPlaceholderText
import com.mikali.crudplayground.ui.createandedit.CreateAndEditPostScreenTopBar
import com.mikali.crudplayground.ui.createandedit.CreateAndEditPostViewModel
import com.mikali.crudplayground.ui.posts.enums.EditMode
import com.mikali.crudplayground.ui.posts.model.PostItem
import com.mikali.crudplayground.ui.posts.viewmodel.PostSharedViewModel
import com.mikali.crudplayground.ui.theme.charcoal
import com.mikali.crudplayground.ui.theme.sandYellow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAndEditPostsScreen(
    editMode: EditMode,
    postSharedViewModel: PostSharedViewModel,
    navController: NavHostController,
    createAndEditPostViewModel: CreateAndEditPostViewModel
) {

    val focusManager = LocalFocusManager.current

    val postUiState: State<PostItem> = createAndEditPostViewModel.postUiState.collectAsState()

    // Setup for create/edit mode
    if (editMode == EditMode.CREATE) {
        postSharedViewModel.clearSinglePostUiState()
    }

    LaunchedEffect(createAndEditPostViewModel.event) {
        createAndEditPostViewModel.event.collectLatest {
            when (it) {
                is CreateAndEditPostViewModel.CreateAndEditPostEvent.OnCreatePostSuccessFul -> {
                    navController.popBackStack()
                    focusManager.clearFocus()
                }
                is CreateAndEditPostViewModel.CreateAndEditPostEvent.OnUpdatePostSuccessFul -> {
                    navController.popBackStack()
                    focusManager.clearFocus()
                }
            }
        }
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
                        println("chris keyboard $keyboardInput")
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
                    createAndEditPostViewModel.onPostButtonClick(
                        editMode = editMode,
                    )
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 2.dp, color = Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = sandYellow)
            ) {
                Text(
                    text = when (editMode) {
                        EditMode.CREATE -> "Save New Post"
                        EditMode.EDIT -> "Save Editing"
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

