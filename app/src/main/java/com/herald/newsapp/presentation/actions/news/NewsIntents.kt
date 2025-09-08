package com.herald.newsapp.presentation.actions.news

import android.content.Context
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.domain.models.HeadlinesModel
import com.herald.newsapp.domain.models.SearchQuery

sealed interface NewsIntents {
    data class OnSearchQueryChanged(val searchQuery: SearchQuery) : NewsIntents
    data class ArticleSaving(val article: HeadlinesModel,val isSearchResult:Boolean = false) : NewsIntents
    data class ErrorOccurred(val exception: Exception) : NewsIntents
    data class OpenHeadline(val headlineUrl: String,val context: Context) : NewsIntents
    data class NavigateToScreen(val route: String) : NewsIntents
    data class FetchNews(val country: String) : NewsIntents
    data class SwitchLanguage(val context: Context, val preferencesManager: PreferencesManager) : NewsIntents
}