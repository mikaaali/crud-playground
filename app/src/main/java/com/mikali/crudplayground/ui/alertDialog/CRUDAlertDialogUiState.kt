package com.mikali.crudplayground.ui.alertDialog

data class CRUDAlertDialogUiState(
    val showDialog: Boolean,
    val title: String = "",
    val text: String = "",
    val onConfirm: () -> Unit = {},
)
