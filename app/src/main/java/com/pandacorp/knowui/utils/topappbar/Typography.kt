package com.pandacorp.knowui.utils.topappbar

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

/**
 * Helper function for component typography tokens.
 */
internal fun Typography.fromToken(value: com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens): TextStyle {
    return when (value) {
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.DisplayLarge -> displayLarge
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.DisplayMedium -> displayMedium
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.DisplaySmall -> displaySmall
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.HeadlineLarge -> headlineLarge
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.HeadlineMedium -> headlineMedium
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.HeadlineSmall -> headlineSmall
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.TitleLarge -> titleLarge
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.TitleMedium -> titleMedium
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.TitleSmall -> titleSmall
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.BodyLarge -> bodyLarge
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.BodyMedium -> bodyMedium
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.BodySmall -> bodySmall
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.LabelLarge -> labelLarge
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.LabelMedium -> labelMedium
        com.pandacorp.knowui.utils.topappbar.tokens.TypographyKeyTokens.LabelSmall -> labelSmall
    }
}