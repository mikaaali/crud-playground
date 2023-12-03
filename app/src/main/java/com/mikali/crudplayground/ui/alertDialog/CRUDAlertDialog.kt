package com.mikali.crudplayground.ui.alertDialog

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun CRUDAlertDialog(uiState: MutableState<CRUDAlertDialogUiState>) {
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Filled.Error, contentDescription = "Error")
        },
        onDismissRequest = {
            uiState.value = uiState.value.copy(showDialog = false)
        },
        title = { Text(uiState.value.title) },
        text = { Text(uiState.value.text) },
        confirmButton = {
            Button(onClick = uiState.value.onConfirm) {
                Text("Dismiss")
            }
        }
    )
}