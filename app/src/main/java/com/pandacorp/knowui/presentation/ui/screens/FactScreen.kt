package com.pandacorp.knowui.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Scaffold(topBar = {
        BackButtonTopAppBar(R.string.fact) {
            navController?.popBackStack()
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(text = currentFactViewModel.fact.value.contentEnglish, color = Color.White)
        }
    }
}