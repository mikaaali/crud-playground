package com.mikali.crudplayground.ui.main.navigation

import com.mikali.crudplayground.ui.screens.posts.enums.EditMode

sealed class ScreenRoutes(val route: String) {
    object Posts : ScreenRoutes("posts")

    class EditPost : ScreenRoutes("editPost/{editMode}") {
        fun createRoute(editMode: EditMode) = "editPost/${editMode.name}"
    }

    object Photos : ScreenRoutes("photos")
}