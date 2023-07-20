package com.pandacorp.knowui.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pandacorp.knowui.R
import com.pandacorp.knowui.presentation.viewmodel.CurrentFactViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactScreen(
    navController: NavController? = null,
    currentFactViewModel: CurrentFactViewModel = koinViewModel(),
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val imageUri = currentFactViewModel.fact.value.imageUri
    val content = currentFactViewModel.fact.value.contentEnglish
    val tags = currentFactViewModel.fact.value.tags

    Scaffold(topBar = {
        BackButtonTopAppBar(R.string.fact) {
            navController?.popBackStack()
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(4.dp)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (isLandscape)
                CardLandscapeContent(imageUri = imageUri, content = content, tags = tags, enableScroll = true)
            else CardPortraitContent(imageUri = imageUri, content = content, tags = tags, enableScroll = true)
        }
    }
}