package com.pandacorp.knowui.presentation.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pandacorp.knowui.R
import com.pandacorp.knowui.data.repository.CustomSharedPreferencesImpl
import com.pandacorp.knowui.domain.models.SavedPreferencesItem
import com.pandacorp.knowui.presentation.ui.dialogs.SettingsDialog
import com.pandacorp.knowui.presentation.ui.theme.GrayBorder
import com.pandacorp.knowui.presentation.ui.theme.KnowUITheme
import com.pandacorp.knowui.presentation.ui.theme.WhiteRippleTheme
import com.pandacorp.knowui.presentation.viewmodel.PreferencesViewModel
import com.pandacorp.knowui.utils.Constants
import com.pandacorp.knowui.utils.getAppVersion
import com.pandacorp.knowui.utils.topappbar.FixedTopAppBar
import com.pandacorp.knowui.utils.topappbar.TopAppBarDefaults
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController? = null,
    savedPreferences: SavedPreferencesItem = SavedPreferencesItem(
        "dark",
        "en"
    )
) {
    val context = LocalContext.current
    val preferencesViewModel: PreferencesViewModel = koinViewModel()

    var openedDialog: String? by rememberSaveable { mutableStateOf(null) }

    when (openedDialog) {
        Constants.Preferences.THEME_KEY ->
            SettingsDialog(
                key = Constants.Preferences.THEME_KEY,
                onValueAppliedListener = {
                    openedDialog = null
                    preferencesViewModel.changeTheme(it)
                }, onDismiss = {
                    openedDialog = null
                })

        Constants.Preferences.LANGUAGE_KEY ->
            SettingsDialog(
                key = Constants.Preferences.LANGUAGE_KEY,
                onValueAppliedListener = {
                    openedDialog = null
                    preferencesViewModel.changeLanguage(it)
                }, onDismiss = {
                    openedDialog = null
                })

        null -> Unit
    }

    Scaffold(topBar = {
        BackButtonTopAppBar(title = R.string.settings) {
            navController?.popBackStack()
        }
    }) { padding ->
        // Use inside of a Box to apply the padding right
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 60.dp)
            ) {
                Text(
                    text = stringResource(R.string.appearance),
                    modifier = Modifier.padding(start = 24.dp, top = 20.dp),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 8.dp),
                    border = GrayBorder,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Column {
                        CardComponent(
                            drawable = R.drawable.ic_theme,
                            text = stringResource(R.string.theme),
                            value = getThemeTitle(context, savedPreferences.theme),
                            onClick = {
                                openedDialog = Constants.Preferences.THEME_KEY
                            }
                        )

                        CardComponent(
                            drawable = R.drawable.ic_language,
                            text = stringResource(R.string.language),
                            value = getLanguageTitle(context, savedPreferences.language),
                            onClick = {
                                openedDialog = Constants.Preferences.LANGUAGE_KEY
                            }
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.another),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, top = 20.dp),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )

                // Retrieve the version from build.gradle
                val version = if (LocalInspectionMode.current)
                // Return a placeholder value for the preview mode
                    "1.0.0-preview"
                else LocalContext.current.getAppVersion()

                Card(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    border = GrayBorder,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    CardComponent(
                        drawable = R.drawable.ic_version,
                        text = stringResource(R.string.version, version)
                    )
                }
            }
        }
    }
}

@Composable
private fun CardComponent(
    modifier: Modifier = Modifier,
    drawable: Int,
    text: String,
    value: String? = null,
    onClick: () -> Unit = {}
) {
    CompositionLocalProvider(LocalRippleTheme provides WhiteRippleTheme()) {
        Row(
            modifier = modifier
                .clickable(onClick = onClick)
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .padding(start = 4.dp, end = 4.dp, bottom = 8.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp),
            )
            @Suppress("DEPRECATION") /* includeFontPadding is set to true by default, but not it is deprecated but still used by Text, so set it to false to remove the wrong padding */
            Column {
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 16.sp,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    color = Color.White,
                )
                if (value != null) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = value,
                        style = TextStyle(
                            fontSize = 14.sp,
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun BackButtonTopAppBar(title: Int = 0, onClick: () -> Unit = {}) {
    FixedTopAppBar(
        title = {
            Text(text = stringResource(id = title))
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back_button))
            }
        }
    )
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview() {
    KnowUITheme {
        SettingsScreen()
    }
}

private fun getThemeTitle(context: Context, key: String): String =
    context.resources.getStringArray(R.array.Themes)
        .toList()[CustomSharedPreferencesImpl.getThemesKeys(context)
        .indexOf(key)]

private fun getLanguageTitle(context: Context, key: String): String =
    context.resources.getStringArray(R.array.Languages)
        .toList()[CustomSharedPreferencesImpl.getLanguagesKeys(context)
        .indexOf(key)]