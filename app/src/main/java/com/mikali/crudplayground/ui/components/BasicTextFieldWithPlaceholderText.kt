package com.mikali.crudplayground.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

/**
 * layering a BasicTextField over a Text composable for the placeholder.
 * The Text composable is defined first, so it's at the bottom.
 * Because the BasicTextField UI looks better than TextField lol
 * The placeholder will only be visible when the BasicTextField is empty.
 */
@Composable
fun BasicTextFieldWithPlaceholderText(
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
            keyboardActions = keyboardActions,
            cursorBrush = SolidColor(Color.White)
        )
    }
}