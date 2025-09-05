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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herald.newsapp.R
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.common.showRetrySnackbar
import com.herald.newsapp.common.toUserFriendlyMessage
import com.herald.newsapp.presentation.actions.news.NewsEvents
import com.herald.newsapp.presentation.actions.news.NewsIntents
import com.herald.newsapp.presentation.components.EmptyScreen
import com.herald.newsapp.presentation.components.LoadingBar
import com.herald.newsapp.presentation.components.LocalizationButton
import com.herald.newsapp.presentation.components.NewsList
import com.herald.newsapp.presentation.states.NewsState
import com.herald.newsapp.presentation.viewmodels.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    country: String,
    lazyListState: LazyListState,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val newsState by newsViewModel.newsState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val retryString = stringResource(R.string.retry)
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                {
                    Text(
                        text = stringResource(R.string.top_stories)
                    )
                }, actions = {
                    LocalizationButton(onClick = { newsViewModel.handleIntent(NewsIntents.SwitchLanguage(context, PreferencesManager(context))) })
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            newsState.apply {
                LoadingBar(isLoading)
                EmptyScreen(news.isNullOrEmpty())
                news?.let { newsItems ->
                    NewsList(
                        newsItems,
                        lazyListState,
                        { newsViewModel.handleIntent(NewsIntents.ArticleSaving(it)) },
                        { newsViewModel.handleIntent(NewsIntents.OpenHeadline(it)) }
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        newsInit(
            newsState,
            { newsViewModel.handleIntent(NewsIntents.FetchNews(country)) },
            { newsViewModel.handleIntent(NewsIntents.ErrorOccurred(it)) }
        )
        newsViewModel.newsEvents.collectLatest {
            when (it) {
                is NewsEvents.ErrorOccurred -> {
                    snackbarHostState.showRetrySnackbar(retryString, it.exception.toUserFriendlyMessage(context)) {
                        newsViewModel.handleIntent(NewsIntents.FetchNews(country))
                    }
                }
            }
        }
    }
}


private inline fun newsInit(
    newsState: NewsState,
    loadInitialData: () -> Unit,
    onError: (Exception) -> Unit
) {
    newsState.apply {
        /**
         * This is supposed to show the initial data if it has not been already loaded
         */
        if (news == null && !isLoading) {
            loadInitialData()
        }
        /**
         * This is supposed to show the retry snackbar on config change only if there was an error already
         */
        exception?.let { onError(it) }
    }
}