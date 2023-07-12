package com.pandacorp.knowui.presentation.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pandacorp.knowui.R
import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.presentation.ui.theme.GrayBorder
import com.pandacorp.knowui.presentation.ui.theme.KnowUITheme
import com.pandacorp.knowui.utils.Constants

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController? = null) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(topBar = {
            MainAppBar {
                navController!!.navigate("SettingsScreen")
            }
        }) { padding ->
            // Use inside of a Box to apply the padding right
            Box(modifier = Modifier.padding(padding)) {
                val facts = List(5) {
                    FactItem(contentEnglish = Constants.loremIpsum)
                }
                VerticalPager(
                    pageCount = facts.size,
                    modifier = Modifier.fillMaxHeight(),
                ) { pageIndex ->
                    CardComponent(facts[pageIndex].contentEnglish)
                }
            }
        }
    }
}

@Composable
private fun CardComponent(content: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        border = GrayBorder
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = content,
                color = Color.White
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun MainAppBar(onClick: () -> Unit = {}) {
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

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AppPreview() {
    KnowUITheme {
        MainScreen()
    }
}