package  com.mikali.crudplayground.ui.edit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mikali.crudplayground.navigation.NavigationScreens
import com.mikali.crudplayground.ui.model.PostItem
import com.mikali.crudplayground.ui.theme.Yellow
import com.mikali.crudplayground.viewmodel.NetworkRequestStatus
import com.mikali.crudplayground.viewmodel.PostSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(currentScreen: MutableState<NavigationScreens>, navController: NavHostController) {

    val focusManager = LocalFocusManager.current

    val viewModel: PostSharedViewModel = viewModel()
    val singlePostUiState: State<PostItem> = viewModel.singlePostUiState.collectAsState()
    val singlePostNetworkStatus: State<NetworkRequestStatus> =
        viewModel.singlePostNetworkRequestStatus.collectAsState()

    // observe single post network status and navigate accordingly based on flow result
    when (singlePostNetworkStatus.value) {
        NetworkRequestStatus.SUCCESS -> {
            focusManager.clearFocus()
            navController.popBackStack()
            currentScreen.value = NavigationScreens.LIST
            viewModel.resetNetworkStatus()
        }

        NetworkRequestStatus.ERROR -> {
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
            viewModel.resetNetworkStatus()
        }

        else -> {} // Don't need to do anything for idle, user did not press the post button
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                        currentScreen.value = NavigationScreens.LIST
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
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
                PlaceholderBasicTextField(
                    value = singlePostUiState.value.title ?: "",
                    onValueChange = { keyboardInput ->
                        viewModel.updateTitle(keyboardInput)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White), // Set background to white
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
                PlaceholderBasicTextField(
                    value = singlePostUiState.value.body ?: "",
                    onValueChange = { keyboardInput ->
                        viewModel.updateBody(keyboardInput)
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

            PostButton(modifier = Modifier.align(Alignment.BottomCenter), // Align to bottom
                onClick = { viewModel.onPostButtonClick(postItem = singlePostUiState.value) }
            )
        }
    }
}

@Composable
private fun PostButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Yellow
        )
    ) {
        Text("Post", color = Color.Black, style = MaterialTheme.typography.titleLarge)
    }
}


/**
 * layering a BasicTextField over a Text composable for the placeholder.
 * The Text composable is defined first, so it's at the bottom.
 * Because the BasicTextField UI looks better than TextField lol
 * The placeholder will only be visible when the BasicTextField is empty.
 */
@Composable
fun PlaceholderBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholderText: String
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
    ) {
        /**
         * Need to hid Text, text will be on the bottom, but if we don't make
         * it disappear, it will still be visible, just behind the BasicTextField
         */
        if (value.isEmpty()) {
            Text(
                text = placeholderText,
                style = textStyle.copy(color = Color.Gray)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}
