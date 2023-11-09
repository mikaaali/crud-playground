package  com.mikali.crudplayground.ui.post

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mikali.crudplayground.navigation.EditMode
import com.mikali.crudplayground.navigation.NavigationScreens
import com.mikali.crudplayground.ui.components.BasicTextFieldWithPlaceholder
import com.mikali.crudplayground.ui.components.TextButton
import com.mikali.crudplayground.ui.components.TopBarWithBack
import com.mikali.crudplayground.ui.post.model.PostItem
import com.mikali.crudplayground.ui.post.viewmodel.PostSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditScreen(
    editMode: EditMode,
    postSharedViewModel: PostSharedViewModel,
    currentScreen: MutableState<NavigationScreens>,
    navController: NavHostController,
) {

    val focusManager = LocalFocusManager.current

    val singlePostUiState: State<PostItem> = postSharedViewModel.singlePostUiState.collectAsState()
    val singlePostNetworkStatus: State<PostCreationEvent> =
        postSharedViewModel.singlePostCreationEvent.collectAsState()

    // observe single post network status and navigate accordingly based on flow result
    // previously, we tried to call pop back and create/update a post in a non-blocking manner
    // so network request to POST/PATCH keeps getting interrupted
    when (singlePostNetworkStatus.value) {
        PostCreationEvent.SUCCESS -> {
            focusManager.clearFocus()
            navController.popBackStack()
            currentScreen.value = NavigationScreens.POSTS
            postSharedViewModel.resetNetworkStatus()
        }

        PostCreationEvent.ERROR -> {
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
        topBar = {
            TopBarWithBack(navController, currentScreen)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp)  // To avoid overlapping with the button
            ) {
                //Title Text
                BasicTextFieldWithPlaceholder(
                    value = singlePostUiState.value.title ?: "",
                    onValueChange = { keyboardInput ->
                        postSharedViewModel.updateTitle(keyboardInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textStyle = MaterialTheme.typography.headlineSmall,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    placeholderText = "Enter title..."
                )

                //Body Text
                BasicTextFieldWithPlaceholder(
                    value = singlePostUiState.value.body ?: "",
                    onValueChange = { keyboardInput ->
                        postSharedViewModel.updateBody(keyboardInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    placeholderText = "Enter body..."
                )
            }

            TextButton(modifier = Modifier.align(Alignment.BottomCenter), // Align to bottom
                onClick = {
                    postSharedViewModel.onPostButtonClick(editMode = editMode, postItem = singlePostUiState.value)
                }
            )
        }
    }
}







