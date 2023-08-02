package com.pandacorp.knowui.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.fragula2.compose.FragulaNavHost
import com.fragula2.compose.rememberFragulaNavController
import com.fragula2.compose.swipeable
import com.pandacorp.knowui.presentation.ui.screens.FactScreen
import com.pandacorp.knowui.presentation.ui.screens.LoginScreen
import com.pandacorp.knowui.presentation.ui.screens.MainScreen
import com.pandacorp.knowui.presentation.ui.screens.SettingsScreen
import com.pandacorp.knowui.presentation.ui.theme.KnowUITheme
import com.pandacorp.knowui.presentation.viewmodel.LoginViewModel
import com.pandacorp.knowui.presentation.viewmodel.PreferencesViewModel
import com.pandacorp.knowui.utils.Constants
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        val preferencesViewModel: PreferencesViewModel by viewModel()

        val language = preferencesViewModel.getLanguage()
        preferencesViewModel.setLanguage(language)
        super.onCreate(savedInstanceState)

        setContent {
            val themeState by preferencesViewModel.themeLiveData.observeAsState(preferencesViewModel.getTheme())
            val theme = themeState.ifEmpty { Constants.Preferences.THEME_DEFAULT }

            KnowUITheme(theme = theme) {
                MainActivityContent()
            }
        }
    }
}

@Composable
private fun MainActivityContent(
    loginViewModel: LoginViewModel = koinViewModel(),
    isSigned: Boolean = loginViewModel.isSigned,
    isSkippedSign: Boolean = loginViewModel.isSkipped,
) {
    // viewModelStoreOwner to provide shared viewmodel for composable functions
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        if (isSigned || isSkippedSign) {
            val navController = rememberFragulaNavController()

            FragulaNavHost(
                navController = navController,
                startDestination = Constants.Screen.MAIN,
            ) {
                swipeable(Constants.Screen.MAIN) {
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides viewModelStoreOwner
                    ) {
                        MainScreen(navController = navController)
                    }
                }
                swipeable(Constants.Screen.SETTINGS) {
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides viewModelStoreOwner
                    ) {
                        SettingsScreen(navController = navController)
                    }
                }
                swipeable(Constants.Screen.FACT) {
                    CompositionLocalProvider(
                        LocalViewModelStoreOwner provides viewModelStoreOwner
                    ) {
                        FactScreen(navController = navController)
                    }
                }
            }
        } else  {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                LoginScreen()
            }
        }
    }
}