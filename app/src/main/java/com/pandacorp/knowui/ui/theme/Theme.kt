package com.pandacorp.knowui.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BlueColorScheme = darkColorScheme(
    primary = BlueTheme_colorPrimary,
    secondary = BlueTheme_colorSecondary,
    background = BlueTheme_colorBackground,
    surface = BlueTheme_colorSurface,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkTheme_colorPrimary,
    secondary = DarkTheme_colorSecondary,
    background = DarkTheme_colorBackground,
    surface = DarkTheme_colorSurface,
)

@Composable
fun KnowUITheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isDark -> DarkColorScheme
        else -> BlueColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.primary.toArgb()

            val insets = WindowCompat.getInsetsController(window, view)
            insets.isAppearanceLightStatusBars = isDark
            insets.isAppearanceLightNavigationBars = isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}