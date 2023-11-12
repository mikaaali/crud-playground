package com.mikali.crudplayground.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = darkTealGreen,
    secondary = darkSandYellow,
    tertiary = darkPeach,
    primaryContainer = darkMintGreen,
    onPrimary = Color.LightGray,
    onSecondary = darkCharcoal,
    background = darkMintGreen,
    surface = Color.LightGray,
    onBackground = darkCharcoal,
    onSurface = darkCharcoal,
)

private val LightColorScheme = lightColorScheme(
    primary = tealGreen, // used frequently, good for primary elements (ex. button, tool bar)
    secondary = sandYellow, // suitable for highlighting secondary elements (ex. navigation drawers, menus, and tabs)
    tertiary = peach, // could be used for accents and tertiary actions (ex. button, text)
    primaryContainer = mintGreen, // background of primary element
    onPrimary = Color.White, // text/icons on primary color
    onSecondary = charcoal, // text/icons on secondary color
    background = mintGreen, // app background
    surface = Color.White, // for cards, sheets, and menus
    onBackground = charcoal, // for text/icons on background
    onSurface = charcoal, // for text/icons on surface
)

@Composable
fun CRUDPlaygroundTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // lol this make me cannot hard code the status bar color
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}