package com.herald.newsapp.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herald.newsapp.R
import com.herald.newsapp.common.showRetrySnackbar
import com.herald.newsapp.domain.models.HeadlinesModel
import com.herald.newsapp.presentation.actions.NewsEvents
import com.herald.newsapp.presentation.actions.NewsIntents
import com.herald.newsapp.presentation.NewsViewModel
import com.herald.newsapp.presentation.components.EmptyScreen
import com.herald.newsapp.presentation.components.LoadingBar
import com.herald.newsapp.presentation.components.NewsItem
import com.herald.newsapp.presentation.states.NewsState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(newsViewModel: NewsViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val newsState by newsViewModel.newsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val retryString = stringResource(R.string.retry)
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar({
                Text(
                    text = stringResource(R.string.top_stories)
                )
            })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            newsState.apply {
                LoadingBar(isLoading)
                EmptyScreen(news.isNullOrEmpty())
                news?.let { newsItems ->
                    NewsList(
                        newsItems,
                        { newsViewModel.handleIntent(NewsIntents.SaveHeadline(it)) },
                        { newsViewModel.handleIntent(NewsIntents.OpenHeadline(it)) }
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        newsInit(
            newsState,
            { newsViewModel.handleIntent(NewsIntents.FetchNews) },
            { newsViewModel.handleIntent(NewsIntents.ErrorOccurred(it)) }
        )
        newsViewModel.newsEvents.collectLatest {
            when (it) {
                is NewsEvents.ErrorOccurred -> {
                    snackbarHostState.showRetrySnackbar(retryString, it.message) {
                        newsViewModel.handleIntent(NewsIntents.FetchNews)
                    }
                }
                is NewsEvents.OpenHeadline -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.headlineUrl)))
                }
            }
        }
    }
}

@Composable
private fun NewsList(
    newsItems: List<HeadlinesModel>,
    onSaveClick: (HeadlinesModel) -> Unit,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        state = rememberLazyListState(),
    ) {
        items(items = newsItems,
            key = { it.id },
            contentType = { HeadlinesModel::class.java }
        ) { item ->
            NewsItem(item, { onSaveClick(item) }, { onItemClick(item.id) })
        }
    }
}

private inline fun newsInit(
    newsState: NewsState,
    loadInitialData: () -> Unit,
    onError: (String) -> Unit
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
        error?.let { onError(it) }
    }
}