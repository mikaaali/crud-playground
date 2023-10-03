package com.mikali.crudplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikali.crudplayground.ui.theme.CRUDPlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CRUDPlaygroundTheme {
                val bottomNavigationScreens =
                    remember { mutableStateOf(BottomNavigationScreens.POST) }
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = "CRUD Playground") }) },
                    bottomBar = {
                        BottomNavigation {
                            BottomNavigationItem(
                                selected = bottomNavigationScreens.value == BottomNavigationScreens.POST,
                                onClick = {
                                    bottomNavigationScreens.value = BottomNavigationScreens.POST
                                },
                                icon = {},
                                label = { Text(text = "Post") }
                            )
                            BottomNavigationItem(
                                selected = bottomNavigationScreens.value == BottomNavigationScreens.IMAGE,
                                onClick = {
                                    bottomNavigationScreens.value = BottomNavigationScreens.IMAGE
                                },
                                icon = {},
                                label = { Text(text = "Image") }
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                    },
                    content = { _ ->
                        when (bottomNavigationScreens.value) {
                            BottomNavigationScreens.POST -> ListScreen()
                            BottomNavigationScreens.IMAGE -> ListOfImageScreen()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ListScreen() {
    val data = listOf(
        "title" to "body",
        "title" to "body",
        "title" to "body",
    )
    ListOfLazyCard(data = data)

}

@Composable
fun ListOfImageScreen() {
}

@Composable
fun ListOfLazyCard(data: List<Pair<String, String>>) {
    LazyColumn {
        items(items = data) { cardItem ->
            CustomCard(
                title = cardItem.first,
                body = cardItem.second,
                onDelete = { },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomCard(
    title: String,
    body: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded = remember { mutableStateOf(false) }
    var isEditing = remember { mutableStateOf(false) }

    Card(
        onClick = { isMenuExpanded.value = true },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title)
            Text(text = body)
        }

        // Dropdown menu
        DropdownMenu(isMenuExpanded, onDelete, onEdit = { isEditing.value = true })
    }
}

@Composable
private fun DropdownMenu(
    isMenuExpanded: MutableState<Boolean>,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    DropdownMenu(
        expanded = isMenuExpanded.value,
        onDismissRequest = { isMenuExpanded.value = false },
    ) {
        DropdownMenuItem(onClick = {
            isMenuExpanded.value = false
            onDelete()
        }) {
            Text("Delete")
        }
        DropdownMenuItem(onClick = {
            isMenuExpanded.value = false
            onEdit()
        }) {
            Text("Edit")
        }
    }
}

@Composable
fun EditScreen() {
    var newTitle = remember { mutableStateOf("") }
    var newBody = remember { mutableStateOf("") }

    TextField(
        value = newTitle.value,
        onValueChange = { newTitle.value = it },
    )
    TextField(
        value = newBody.value,
        onValueChange = { newBody.value = it },
    )
    Button(onClick = { }) {
        Text("Save")
    }
}

enum class BottomNavigationScreens {
    POST,
    IMAGE,
}