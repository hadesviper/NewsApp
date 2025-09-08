package com.herald.newsapp.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herald.newsapp.R
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.common.showRetrySnackbar
import com.herald.newsapp.common.toUserFriendlyMessage
import com.herald.newsapp.domain.models.SearchQuery
import com.herald.newsapp.presentation.actions.news.NewsEvents
import com.herald.newsapp.presentation.actions.news.NewsIntents
import com.herald.newsapp.presentation.screens.common.EmptyScreen
import com.herald.newsapp.presentation.screens.common.LoadingBar
import com.herald.newsapp.presentation.screens.common.LocalizationButton
import com.herald.newsapp.presentation.screens.common.NewsList
import com.herald.newsapp.presentation.viewmodels.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    searchQuery: MutableState<SearchQuery>,
    newsViewModel: NewsViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val newsState by newsViewModel.newsSearchingState.collectAsStateWithLifecycle()
    val allCategories = stringArrayResource(R.array.categories)
    val allCategoriesEn = stringArrayResource(R.array.categories_en)
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { SearchTopBar(searchQuery,newsViewModel::handleIntent) }
    ) {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            CategoriesRow(allCategories.toList(), allCategoriesEn.toList(), searchQuery)
            newsState.apply {
                LoadingBar(isLoading)
                EmptyScreen(news.isNullOrEmpty())
                news?.let { newsItems ->
                    NewsList(
                        newsItems,
                        lazyListState,
                        {
                            keyboardController?.hide()
                            newsViewModel.handleIntent(NewsIntents.ArticleSaving(it,true))
                        },
                        {
                            keyboardController?.hide()
                            newsViewModel.handleIntent(NewsIntents.OpenHeadline(it,context))
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        newsViewModel.newsEvents.collectLatest {
            when (it) {
                is NewsEvents.ErrorOccurred -> {
                    keyboardController?.hide()
                    snackbarHostState.showRetrySnackbar(null, it.exception.toUserFriendlyMessage(context),null)
                }
            }
        }
    }

    /**
     * This is supposed to show the retry snackbar on config change only if there was an error already
     */
    newsState.exception?.let { newsViewModel.handleIntent(NewsIntents.ErrorOccurred(it)) }
}

@Composable
private fun CategoriesRow(categories: List<String>, categoriesEn: List<String>, searchQuery: MutableState<SearchQuery>) {
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        Text (stringResource(R.string.filter_categories), style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)))
        {
            items(categories.size) { index ->
                val isSelected = categoriesEn[index] in searchQuery.value.categories
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        searchQuery.value = searchQuery.value.copy(categories = if (isSelected) {
                            searchQuery.value.categories - categoriesEn[index]
                        } else {
                            searchQuery.value.categories + categoriesEn[index]
                        })
                    },
                    label = { Text(text = categories[index]) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    searchQuery: MutableState<SearchQuery>,
    handleIntents: (NewsIntents) -> Unit)
{
    val context = LocalContext.current
    val searchNews = { handleIntents(NewsIntents.OnSearchQueryChanged(searchQuery.value)) }
    TopAppBar(
        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_large)),
        title =
        {
            SearchTextField(
                query = searchQuery.value.query,
                onQueryChange = {
                    searchQuery.value = searchQuery.value.copy(query = it)
                    searchNews()
                },
                onClear = {
                    searchQuery.value = searchQuery.value.copy(query = "")
                    searchNews()
                }
            )
        },
        actions = {
            LocalizationButton {handleIntents(NewsIntents.SwitchLanguage(context, PreferencesManager(context)))  }
        }
    )
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search)
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(hint) },
        singleLine = true,
        trailingIcon = {
            AnimatedVisibility(
                query.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onClear) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_l)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(end = dimensionResource(R.dimen.padding_medium))
    )
}