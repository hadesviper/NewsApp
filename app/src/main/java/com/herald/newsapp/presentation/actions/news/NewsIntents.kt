package com.herald.newsapp.presentation.actions.news

import android.content.Context
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.domain.models.HeadlinesModel

sealed interface NewsIntents {
    data class OnSearchQueryChanged(val query: String) : NewsIntents
    data class ArticleSaving(val article: HeadlinesModel) : NewsIntents
    data class ErrorOccurred(val exception: Exception) : NewsIntents
    data class OpenHeadline(val headlineUrl: String) : NewsIntents
    data class NavigateToScreen(val route: String) : NewsIntents
    data class FetchNews(val country: String) : NewsIntents
    data class SwitchLanguage(val context: Context, val preferencesManager: PreferencesManager) : NewsIntents
    data object FetchSavedArticle : NewsIntents
}