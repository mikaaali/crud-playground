package com.mikali.crudplayground.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NavigationViewModel : ViewModel() {

    // Using StateFlow instead of MutableState as it's lifecycle-aware and thread-safe.
    private val _showPostScreenActionDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPostScreenActionDialog: StateFlow<Boolean> = _showPostScreenActionDialog

    // Function to control the dialog visibility.
    fun setShowListScreenDialog(show: Boolean) {
        _showPostScreenActionDialog.value = show
    }


    private val _editMode: MutableStateFlow<EditMode> = MutableStateFlow(EditMode.CREATE)
    val editMode: StateFlow<EditMode> = _editMode

    fun setEditMode(mode: EditMode) {
        _editMode.value = mode
    }
}

enum class EditMode {
    CREATE, EDIT
}