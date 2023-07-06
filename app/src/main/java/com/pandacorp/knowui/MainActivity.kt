package com.pandacorp.knowui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.fragula2.compose.FragulaNavHost
import com.fragula2.compose.rememberFragulaNavController
import com.fragula2.compose.swipeable
import com.pandacorp.knowui.screens.MainScreen
import com.pandacorp.knowui.ui.theme.KnowUITheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KnowUITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberFragulaNavController()
                    Scaffold(topBar = {
                        AppBar()
                    }) { padding ->
                        Box(modifier = Modifier.padding(padding)) { // Use inside of Box to apply padding right
                            FragulaNavHost(
                                navController = navController,
                                startDestination = "MainScreen",
                            ) {
                                swipeable("MainScreen") {
                                    MainScreen(navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AppBar(
    onClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },

        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            IconButton(onClick = onClick) {
                Icon(Icons.Default.Settings, contentDescription = stringResource(id = R.string.settings))
            }
        }
    )
}