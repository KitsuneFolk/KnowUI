package com.pandacorp.knowui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pandacorp.knowui.Constants
import com.pandacorp.knowui.FactItem
import com.pandacorp.knowui.ui.theme.KnowUITheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController? = null) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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

@Composable
fun CardComponent(content: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = content
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AppPreview() {
    KnowUITheme {
        MainScreen()
    }
}