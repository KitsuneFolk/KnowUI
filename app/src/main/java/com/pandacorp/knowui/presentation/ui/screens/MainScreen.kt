package com.pandacorp.knowui.presentation.ui.screens

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pandacorp.knowui.R
import com.pandacorp.knowui.domain.models.FactItem
import com.pandacorp.knowui.domain.models.FactState
import com.pandacorp.knowui.presentation.ui.theme.GrayBorder
import com.pandacorp.knowui.presentation.ui.theme.KnowUITheme
import com.pandacorp.knowui.presentation.viewmodel.CurrentFactViewModel
import com.pandacorp.knowui.presentation.viewmodel.FactsViewModel
import com.pandacorp.knowui.utils.Animations
import com.pandacorp.knowui.utils.Constants
import com.pandacorp.knowui.utils.formatTags
import com.pandacorp.knowui.utils.topappbar.FixedTopAppBar
import com.pandacorp.knowui.utils.topappbar.TopAppBarDefaults
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController? = null,
    factsViewModel: FactsViewModel = koinViewModel(),
    currentFactViewModel: CurrentFactViewModel = koinViewModel(),
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(
            topBar = {
                MainAppBar {
                    navController!!.navigate(Constants.Screen.SETTINGS)
                }
            }) { padding ->
            // Use inside of a Box to apply the padding right
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                val facts = factsViewModel.facts.value

                if (facts is FactState.Loading) {
                    Pager(isShowPlaceholder = true)
                } else {
                    Pager(
                        items = facts.data ?: emptyList(),
                        isLoadMore = factsViewModel.isStopLoading.value,
                        onLoadMore = {
                            factsViewModel.loadMoreFacts()
                        },
                        onFactClick = { fact ->
                            currentFactViewModel.setFact(fact)
                            navController!!.navigate(Constants.Screen.FACT)
                        })
                    if (facts is FactState.Error) {
                        Snackbar(
                            modifier = Modifier
                                .padding(bottom = 40.dp, start = 15.dp, end = 15.dp)
                                .align(Alignment.BottomCenter),
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            Text(
                                text = stringResource(id = R.string.loading_error),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Pager(
    items: List<FactItem> = emptyList(),
    isShowPlaceholder: Boolean = false,
    buffer: Int = 1,
    isLoadMore: Boolean = false,
    onLoadMore: () -> Unit = {},
    onFactClick: (fact: FactItem) -> Unit = {},
) {
    val pagerState = rememberPagerState()

    val facts: List<FactItem> = if (isShowPlaceholder) {
        val placeholderFact = FactItem()
        List(5) { placeholderFact }
    } else {
        if (isLoadMore) {
            val isAtTargetValue by remember(pagerState.currentPage) {
                mutableStateOf(pagerState.currentPage == items.size - 1 - buffer)
            }
            LaunchedEffect(isAtTargetValue) {
                if (isAtTargetValue) onLoadMore()
            }
        }
        items
    }

    VerticalPager(
        pageCount = facts.size,
        modifier = Modifier.fillMaxHeight(),
        state = pagerState,
    ) { pageIndex ->
        val factItem = facts[pageIndex]
        CardComponent(
            isPlaceHolder = isShowPlaceholder,
            isReachedEnd = ((pageIndex == facts.size - 1) && !isLoadMore),
            content = factItem.contentEnglish,
            imageUri = factItem.imageUri,
            tags = factItem.tags
        ) {
            onFactClick(facts[pageIndex])
        }
    }
}

@Composable
private fun CardComponent(
    modifier: Modifier = Modifier,
    isPlaceHolder: Boolean = true,
    isReachedEnd: Boolean = false,
    imageUri: Uri?,
    content: String,
    tags: List<String>,
    onClick: () -> Unit = {},
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column {
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = modifier
                .then(
                    if (isReachedEnd) Modifier.fillMaxHeight(if (isLandscape) 0.75f else 0.9f)
                    else Modifier.fillMaxHeight()
                )
                .fillMaxWidth()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable(onClick = {
                    if (!isPlaceHolder && imageUri != null) onClick()
                }),
            shape = RoundedCornerShape(20.dp),
            border = GrayBorder
        ) {
            if (isPlaceHolder) CardPlaceholderContent()
            else {
                if (isLandscape) CardLandscapeContent(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    imageUri = imageUri,
                    content = content,
                    tags = tags
                )
                else CardPortraitContent(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    imageUri = imageUri,
                    content = content,
                    tags = tags
                )
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

@Composable
fun CardPlaceholderContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
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
    }
}

@Composable
fun CardLandscapeContent(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    content: String,
    tags: List<String>,
    enableScroll: Boolean = false,
) {
    var isLoading by remember {
        mutableStateOf(true)
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .then(
                    if (isLoading) Modifier
                        .shimmer(
                            rememberShimmer(
                                ShimmerBounds.View,
                                theme = Animations.ShimmerTheme
                            )
                        ) else Modifier
                )
        ) {
            if (isLoading) { // Show a loading text
                Text(modifier = Modifier.align(Alignment.Center), text = stringResource(id = R.string.loading))
            }
            AsyncImage(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.4f),
                model = imageUri,
                onSuccess = {
                    isLoading = false
                },
                contentDescription = null,
            )
        }
        Column {
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
                    .then(
                        if (enableScroll) Modifier.verticalScroll(rememberScrollState()) else Modifier
                    ),
                text = content,
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                text = formatTags(tags = tags),
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CardPortraitContent(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    content: String,
    tags: List<String>,
    enableScroll: Boolean = false,
) {
    var isLoading by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .then(
                    if (isLoading) Modifier
                        .shimmer(
                            rememberShimmer(
                                ShimmerBounds.View,
                                theme = Animations.ShimmerTheme
                            )
                        ) else Modifier
                )
        ) {
            if (isLoading) { // Show a loading text
                Text(modifier = Modifier.align(Alignment.Center), text = stringResource(id = R.string.loading))
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth(1f),
                model = imageUri,
                onSuccess = {
                    isLoading = false
                },
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 6.dp)
                .weight(1f)
                .then(
                    if (enableScroll) Modifier.verticalScroll(rememberScrollState()) else Modifier
                ),
            text = content,
            color = Color.White,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
            text = formatTags(tags = tags),
            color = Color.White,
            overflow = TextOverflow.Ellipsis
        )
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