package com.herald.newsapp.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herald.newsapp.R
import com.herald.newsapp.presentation.NewsViewModel
import com.herald.newsapp.presentation.actions.NewsEvents
import com.herald.newsapp.presentation.actions.NewsIntents
import com.herald.newsapp.presentation.components.EmptyScreen
import com.herald.newsapp.presentation.components.NewsList
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedArticlesScreen(
    newsViewModel: NewsViewModel,
    lazyListState: LazyListState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val newsState by newsViewModel.savedArticlesState.collectAsStateWithLifecycle()
    val context = LocalContext.current
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
            newsState.let { newsItems ->
                if (newsItems.isEmpty())
                    EmptyScreen(true)
                else
                    NewsList(
                        newsItems,
                        lazyListState,
                        { newsViewModel.handleIntent(NewsIntents.ArticleSaving(it)) },
                        { newsViewModel.handleIntent(NewsIntents.OpenHeadline(it)) }
                    )
            }
        }
    }

    LaunchedEffect(Unit) {
        newsViewModel.newsEvents.collectLatest {
            when (it) {
                is NewsEvents.OpenHeadline -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.headlineUrl)))
                }
                is NewsEvents.ShowToast -> {
                    Toast.makeText(context, it.messageResID, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}