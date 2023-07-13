package com.pandacorp.knowui.utils.topappbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Returns the new background [Color] to use, representing the original background [color] with an
 * overlay corresponding to [elevation] applied. The overlay will only be applied to
 * [ColorScheme.surface].
 */
internal fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
    return if (backgroundColor == surface) {
        surfaceColorAtElevation(elevation)
    } else {
        backgroundColor
    }
}

internal val WindowInsets.Companion.systemBarsForVisualComponents: WindowInsets
    @Composable
    get() = systemBars