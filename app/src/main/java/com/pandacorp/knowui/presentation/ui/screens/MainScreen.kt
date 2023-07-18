package com.pandacorp.knowui.presentation.ui.screens

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pandacorp.knowui.R
import com.pandacorp.knowui.data.models.FactState
import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.presentation.ui.theme.GrayBorder
import com.pandacorp.knowui.presentation.ui.theme.KnowUITheme
import com.pandacorp.knowui.presentation.viewmodel.FactsViewModel
import com.pandacorp.knowui.utils.Animations
import com.pandacorp.knowui.utils.topappbar.FixedTopAppBar
import com.pandacorp.knowui.utils.topappbar.TopAppBarDefaults
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(factsViewModel: FactsViewModel = koinViewModel(), navController: NavController? = null) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(
            topBar = {
                MainAppBar {
                    navController!!.navigate("SettingsScreen")
                }
            }) { padding ->
            // Use inside of a Box to apply the padding right
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                when (val facts = factsViewModel.facts.value) {
                    is FactState.Success -> {
                        Pager(items = facts.data!!, isLoadMore = factsViewModel.isStopLoading.value) {
                            factsViewModel.loadMoreFacts()
                        }
                    }

                    is FactState.Error -> {
                        Snackbar(
                            modifier = Modifier
                                .padding(bottom = 40.dp, start = 15.dp, end = 15.dp)
                                .align(Alignment.BottomCenter), containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            Text(
                                text = stringResource(id = R.string.loading_error),
                                color = Color.White
                            )
                        }
                    }

                    is FactState.Loading -> {
                        Pager(items = listOf(), isShowPlaceholder = true)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Pager(
    items: List<FactItem>,
    isShowPlaceholder: Boolean = false,
    buffer: Int = 1,
    isLoadMore: Boolean = false,
    onLoadMore: (() -> Unit) = {},
) {
    val pagerState = rememberPagerState()

    val facts: List<FactItem>
    if (isShowPlaceholder) {
        val placeholderFact = FactItem()
        facts = List(5) { placeholderFact }
    } else {
        facts = items
        if (isLoadMore) {
            val isAtTargetValue by remember(pagerState.currentPage) {
                mutableStateOf(pagerState.currentPage == items.size - 1 - buffer)
            }
            LaunchedEffect(isAtTargetValue) {
                if (isAtTargetValue) onLoadMore()
            }
        }
    }

    VerticalPager(
        pageCount = facts.size,
        modifier = Modifier.fillMaxHeight(),
        state = pagerState,
    ) { pageIndex ->
        CardComponent(
            isPlaceHolder = isShowPlaceholder,
            isReachedEnd = ((pageIndex == facts.size - 1) && !isLoadMore),
            content = facts[pageIndex].contentEnglish
        )
    }
}

@Composable
private fun CardComponent(
    modifier: Modifier = Modifier,
    isPlaceHolder: Boolean = true,
    isReachedEnd: Boolean = false,
    content: String,
    onClick: () -> Unit = {},
) {
    val isLandscape =  LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    Column {
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = modifier
                .then(
                    if (isReachedEnd)
                        Modifier.fillMaxHeight(if (isLandscape) 0.75f else 0.9f)
                    else Modifier.fillMaxHeight()
                )
                .fillMaxWidth()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(20.dp),
            border = GrayBorder
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .then(
                        if (isPlaceHolder) Modifier
                        else Modifier.padding(16.dp)
                    )
            ) {
                if (isPlaceHolder) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .shimmer(
                                rememberShimmer(ShimmerBounds.View, theme = Animations.ShimmerTheme)
                            )
                            .padding(top = 4.dp),
                        text = stringResource(id = R.string.loading),
                        fontSize = 18.sp,
                        color = Color.White
                    )
                } else Text(text = content, color = Color.White)
            }
        }
        if (isReachedEnd) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 8.dp),
                text = stringResource(id = R.string.end_reached),
                color = Color.White
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun MainAppBar(onClick: () -> Unit = {}) {
    FixedTopAppBar(
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