package com.mikali.crudplayground.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * ChipIcon is a composable that displays an icon with a light tint when selected.
 */
@Composable
fun ChipIcon(
    icon: ImageVector,
    selected: Boolean,
    enabledColor: Color
) {
    if (selected) {
        Surface(
            color = enabledColor.copy(alpha = 0.2f), // Light tint for the chip background
            shape = CircleShape
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = enabledColor,
                modifier = Modifier.padding(4.dp)
            )
        }
    } else {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}