package com.mikali.crudplayground.ui.posts.editview

import android.widget.Toast
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mikali.crudplayground.ui.common.BasicTextFieldWithPlaceholderText
import com.mikali.crudplayground.ui.posts.enums.EditMode
import com.mikali.crudplayground.ui.posts.enums.SinglePostNetworkStatus
import com.mikali.crudplayground.ui.posts.model.PostItem
import com.mikali.crudplayground.ui.posts.viewmodel.PostSharedViewModel
import com.mikali.crudplayground.ui.theme.charcoal
import com.mikali.crudplayground.ui.theme.sandYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen(
    editMode: EditMode,
    postSharedViewModel: PostSharedViewModel,
    navController: NavHostController,
) {

    val focusManager = LocalFocusManager.current

    val singlePostUiState: State<PostItem> = postSharedViewModel.singlePostUiState.collectAsState()
    val singlePostNetworkStatus: State<SinglePostNetworkStatus> =
        postSharedViewModel.singleSinglePostNetworkStatus.collectAsState()

    // observe single post network status and navigate accordingly based on flow result
    // previously, we tried to call pop back and create/update a post in a non-blocking manner
    // so network request to POST/PATCH keeps getting interrupted
    when (singlePostNetworkStatus.value) {
        SinglePostNetworkStatus.SUCCESS -> {
            focusManager.clearFocus()
            navController.popBackStack()
            postSharedViewModel.resetNetworkStatus()
        }

        SinglePostNetworkStatus.ERROR -> {
            /**
             * TODO-better looking error UI, toast auto disappears after sometime,
             * but other custom UI will not, so reset NetworkRequestStatus to Idle,
             * otherwise, the UI just retain there, maybe use some animation UI in the future
             */
            Toast.makeText(
                LocalContext.current,
                "not able to upload a new post to the server",
                Toast.LENGTH_SHORT
            ).show()
            postSharedViewModel.resetNetworkStatus()
        }

        else -> {} // Don't need to do anything for idle, user did not press the post button
    }

    // Setup for create/edit mode
    if (editMode == EditMode.CREATE) {
        postSharedViewModel.clearSinglePostUiState()
    }

    Scaffold(
        topBar = { EditScreenTopBar(navController) }
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
                    value = singlePostUiState.value.title ?: "",
                    onValueChange = { keyboardInput ->
                        postSharedViewModel.updateTitle(keyboardInput)
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
                    value = singlePostUiState.value.body ?: "",
                    onValueChange = { keyboardInput ->
                        postSharedViewModel.updateBody(keyboardInput)
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
                    postSharedViewModel.onPostButtonClick(
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

