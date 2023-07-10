package com.pandacorp.knowui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fragula2.compose.FragulaNavHost
import com.fragula2.compose.rememberFragulaNavController
import com.fragula2.compose.swipeable
import com.pandacorp.knowui.data.CustomSharedPreferences
import com.pandacorp.knowui.data.SavedPreferencesItem
import com.pandacorp.knowui.screens.MainScreen
import com.pandacorp.knowui.screens.SettingsScreen
import com.pandacorp.knowui.ui.theme.KnowUITheme

class MainActivity : AppCompatActivity() {
    private val sp: CustomSharedPreferences by lazy {
        CustomSharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Throw any exceptions that weren't caught
        Thread.setDefaultUncaughtExceptionHandler { _, throwable -> throw (throwable) }

        sp.setLanguage() // TODO: Maybe don't store by myself but Android, idk if it will work on APi 33, because it requires setLanguage after onCreate(), here is the sample app: https://github.com/android/user-interface-samples/blob/main/PerAppLanguages/compose_app/app/src/main/java/com/example/perapplanguages/MainActivity.kt

        super.onCreate(savedInstanceState)

        setContent {
            var savedPreferences by remember { mutableStateOf(sp.getSavedPreferences()) }
            sp.registerOnChangeListener { _, _ -> savedPreferences = sp.getSavedPreferences() }
            KnowUITheme(theme = savedPreferences.theme) {
                MainActivityContent(savedPreferences)
            }
        }
    }

    override fun onDestroy() {
        sp.unregisterOnChangeListener()
        super.onDestroy()
    }
}

@Composable
private fun MainActivityContent(
    savedPreferences: SavedPreferencesItem = SavedPreferencesItem(
        "dark",
        "en",
        "DarkTheme Preview",
        "English Preview"
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