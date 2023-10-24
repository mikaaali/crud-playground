package  com.mikali.crudplayground.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mikali.crudplayground.ui.model.PostItem
import com.mikali.crudplayground.ui.theme.Yellow
import com.mikali.crudplayground.viewmodel.PostSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavHostController) {

    val viewModel: PostSharedViewModel = viewModel()
    val singlePostUiState: State<PostItem> = viewModel.singlePostUiState.collectAsState()

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
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
                textStyle = MaterialTheme.typography.titleLarge,
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
                    .height(100.dp)
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

            //TODO- Add this back once learn how to upload an image to server
//            ImageGrid(
//                images = listOf(
//                    R.drawable.ic_launcher_background,
//                    R.drawable.ic_launcher_background,
//                    R.drawable.ic_launcher_background,
//                    R.drawable.ic_launcher_background,
//                    R.drawable.ic_launcher_background,
//                    R.drawable.ic_launcher_background,
//                    R.drawable.ic_launcher_background
//                ), onAddImageClicked = {/*TODO*/ })

            PostButton(onClick = {
                /**
                 * TODO- better practice, track saving status, Loading, Success, and Error
                 * help UI know what is happening in the back if we successfully uploaded
                 * a post or not. note, don't confuse this with uiState for postInput
                 * don't really need to know postInput success thingamajig, becuase it's
                 * UI thread update, not on some background thread where we need to handle
                 * async stuff where background unseen stuff get's blocked etc.
                 */
                viewModel.onPostButtonClick(postItem = singlePostUiState.value)
                /**
                 * the following two line is causing the HTTP FAILED: java.io.IOException: Canceled error
                 * where we can't post to the server successfully
                 */
                // Close the keyboard and navigate back
//                focusManager.clearFocus()
//                navController.popBackStack()
            })
        }
    }
}

@Composable
private fun PostButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Yellow
        )
    ) {
        Text("Post", color = Color.Black, style = MaterialTheme.typography.titleMedium)
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

enum class ButtonClickType {
    CREATE, UPDATE
}
