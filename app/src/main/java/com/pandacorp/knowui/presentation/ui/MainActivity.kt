package com.pandacorp.knowui.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fragula2.compose.FragulaNavHost
import com.fragula2.compose.rememberFragulaNavController
import com.fragula2.compose.swipeable
import com.pandacorp.knowui.domain.models.SavedPreferencesItem
import com.pandacorp.knowui.presentation.ui.screens.MainScreen
import com.pandacorp.knowui.presentation.ui.screens.SettingsScreen
import com.pandacorp.knowui.presentation.ui.theme.KnowUITheme
import com.pandacorp.knowui.presentation.viewmodel.PreferencesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val preferencesViewModel: PreferencesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedPreferences = preferencesViewModel.getSavedPreferences()
        preferencesViewModel.setLanguage(savedPreferences.language)
        super.onCreate(savedInstanceState)

        setContent {
            val theme by preferencesViewModel.themeLiveData.observeAsState(savedPreferences.theme)
            val language by preferencesViewModel.languageLiveData.observeAsState(savedPreferences.language)

            val rememberPreferences = remember(theme, language) {
                SavedPreferencesItem(theme = theme, language = language)
            }

            KnowUITheme(theme = theme) {
                MainActivityContent(rememberPreferences)
            }
        }
    }


}

@Composable
private fun MainActivityContent(
    savedPreferences: SavedPreferencesItem = SavedPreferencesItem(
        "dark",
        "en"
    ),
) {
    val navController = rememberFragulaNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        FragulaNavHost(
            navController = navController,
            startDestination = "MainScreen",
        ) {
            swipeable("MainScreen") {
                MainScreen(navController = navController)
            }
            swipeable("SettingsScreen") {
                SettingsScreen(navController = navController, savedPreferences = savedPreferences)
            }
        }
    }
}

@Preview
@Composable
private fun MainActivityPreview() {
    KnowUITheme {
        MainActivityContent()
    }
}