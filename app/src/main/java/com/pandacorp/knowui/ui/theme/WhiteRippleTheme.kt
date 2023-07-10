package com.pandacorp.knowui.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * White ripple theme for a composable to set White ripple color, here's an example:
 * ```
 * CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme()) {
 *  Button() {}
 * }
 * ```
 */
class WhiteRippleTheme : RippleTheme {
    private val color = Color.White

    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            color,
            lightTheme = false
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            color,
            lightTheme = false
        )
}