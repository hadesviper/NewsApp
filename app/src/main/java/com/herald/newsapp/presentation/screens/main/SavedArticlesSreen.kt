package com.herald.newsapp.presentation.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herald.newsapp.R
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.presentation.actions.news.NewsIntents
import com.herald.newsapp.presentation.screens.common.EmptyScreen
import com.herald.newsapp.presentation.screens.common.LocalizationButton
import com.herald.newsapp.presentation.screens.common.NewsList
import com.herald.newsapp.presentation.viewmodels.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedArticlesScreen(
    lazyListState: LazyListState,
    newsViewModel: NewsViewModel
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val newsState by newsViewModel.savedArticlesState.collectAsStateWithLifecycle()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                {
                    Text(
                        text = stringResource(R.string.saved_news)
                    )
                },
                actions = {
                    LocalizationButton(onClick = { newsViewModel.handleIntent(NewsIntents.SwitchLanguage(context, PreferencesManager(context))) })
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            newsState.let { newsItems ->
                if (newsItems.isEmpty())
                    EmptyScreen(true)
                else
                    NewsList(
                        newsItems,
                        lazyListState,
                        { newsViewModel.handleIntent(NewsIntents.ArticleSaving(it)) },
                        { newsViewModel.handleIntent(NewsIntents.OpenHeadline(it,context)) }
                    )
            }
        }
    }
}