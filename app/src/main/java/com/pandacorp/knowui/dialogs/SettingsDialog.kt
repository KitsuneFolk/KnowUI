package com.pandacorp.knowui.dialogs

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.pandacorp.knowui.R
import com.pandacorp.knowui.data.CustomSharedPreferences
import com.pandacorp.knowui.ui.theme.WhiteRippleTheme

@Composable
fun SettingsDialog(
    key: String = CustomSharedPreferences.THEME_KEY,
    onValueAppliedListener: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current

    val title: String
    val itemsList: List<SettingsDialogItem>
    val isRoundedImage: Boolean
    if (key == CustomSharedPreferences.THEME_KEY) {
        itemsList = fillThemesList(context)
        title = stringResource(R.string.theme)
        isRoundedImage = true
    } else {
        itemsList = fillLanguagesList(context)
        title = stringResource(R.string.language)
        isRoundedImage = false
    }

    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .heightIn(max = 350.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),
                    color = Color.White
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .weight(1f)
                ) {
                    items(itemsList) { item ->
                        ItemComponent(
                            isRoundedImage = isRoundedImage,
                            title = item.title,
                            drawable = item.drawable
                        ) {
                            onValueAppliedListener(item.key)
                        }
                    }
                }
                CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme()) {
                    Button(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(bottom = 16.dp, end = 16.dp),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.White),
                        onClick = onDismiss,
                    ) {
                        Text(text = stringResource(id = R.string.cancel), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemComponent(
    modifier: Modifier = Modifier,
    isRoundedImage: Boolean = false,
    title: String,
    drawable: Drawable,
    onClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .indication(
                    // Change the ripple color to white
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        color = Color.White,
                        radius = 8.dp
                    )
                )
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = rememberDrawablePainter(drawable),
                contentDescription = title,
                modifier = Modifier
                    .padding(start = 20.dp, end = 4.dp)
                    .padding(vertical = 6.dp)
                    .then(
                        if (isRoundedImage) Modifier.clip(RoundedCornerShape(50.dp))
                        else Modifier
                    )
                    .size(32.dp),
            )
            Text(
                text = title,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically),
                color = Color.White
            )
        }
    }
}

private fun fillThemesList(context: Context): List<SettingsDialogItem> {
    val keysList = CustomSharedPreferences(context).getThemesKeys()
    val titlesList = context.resources.getStringArray(R.array.Themes)
    val itemsList = context.resources.obtainTypedArray(R.array.Themes_drawables)

    val themesList = mutableListOf<SettingsDialogItem>()
    repeat(keysList.size) { i ->
        themesList.add(
            SettingsDialogItem(
                keysList[i],
                titlesList[i],
                itemsList.getDrawable(i)!!
            )
        )
    }
    itemsList.recycle()
    return themesList
}

private fun fillLanguagesList(context: Context): List<SettingsDialogItem> {
    val keysList = CustomSharedPreferences(context).getLanguagesKeys()

    val titlesList = context.resources.getStringArray(R.array.Languages)
    val drawablesList = context.resources.obtainTypedArray(R.array.Languages_drawables)

    val languagesList = mutableListOf<SettingsDialogItem>()
    repeat(keysList.size) { i ->
        languagesList.add(
            SettingsDialogItem(
                keysList[i],
                titlesList[i],
                drawablesList.getDrawable(i)!!
            )
        )
    }
    drawablesList.recycle()
    return languagesList
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ThemeDialogPreview() {
    SettingsDialog(key = CustomSharedPreferences.THEME_KEY)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LanguageDialogPreview() {
    SettingsDialog(key = CustomSharedPreferences.LANGUAGE_KEY)
}