package com.pandacorp.knowui.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.pandacorp.knowui.utils.Constants

private val BlueColorScheme =
    darkColorScheme(
        primary = BlueTheme_colorPrimary,
        secondary = BlueTheme_colorSecondary,
        background = BlueTheme_colorBackground,
        surface = BlueTheme_colorSurface,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = DarkTheme_colorPrimary,
        secondary = DarkTheme_colorSecondary,
        background = DarkTheme_colorBackground,
        surface = DarkTheme_colorSurface,
    )

private val PurpleColorScheme =
    darkColorScheme(
        primary = PurpleTheme_colorPrimary,
        secondary = PurpleTheme_colorSecondary,
        background = PurpleTheme_colorBackground,
        surface = PurpleTheme_colorSurface,
    )

private val RedColorScheme =
    darkColorScheme(
        primary = RedTheme_colorPrimary,
        secondary = RedTheme_colorSecondary,
        background = RedTheme_colorBackground,
        surface = RedTheme_colorSurface,
    )

@Composable
fun KnowUITheme(
    theme: String = Constants.Preferences.THEME_DEFAULT,
    content: @Composable () -> Unit
) {
    val colorScheme =
        when (theme) {
            Constants.Preferences.THEME_FOLLOW_SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else BlueColorScheme
            Constants.Preferences.THEME_BLUE -> BlueColorScheme
            Constants.Preferences.THEME_DARK -> DarkColorScheme
            Constants.Preferences.THEME_PURPLE -> PurpleColorScheme
            Constants.Preferences.THEME_RED -> RedColorScheme
            else -> throw IllegalArgumentException("Unresolved value = $theme")
        }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.primary.toArgb()

            val insets = WindowCompat.getInsetsController(window, view)
            insets.isAppearanceLightStatusBars = false
            insets.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}