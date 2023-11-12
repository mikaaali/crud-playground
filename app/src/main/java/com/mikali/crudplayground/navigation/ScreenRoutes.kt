package com.mikali.crudplayground.navigation

import com.mikali.crudplayground.ui.posts.enums.EditMode

sealed class Screen(val route: String) {
    object Posts : Screen("posts")

    class EditPost : Screen("editPost/{editMode}") {
        fun createRoute(editMode: EditMode) = "editPost/${editMode.name}"
    }

    object Photos : Screen("photos")
}