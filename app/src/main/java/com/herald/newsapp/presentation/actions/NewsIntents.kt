package com.herald.newsapp.presentation.actions

import com.herald.newsapp.domain.models.HeadlinesModel

sealed interface NewsIntents {
    data class OnSearchQueryChanged (val query: String): NewsIntents
    data class ArticleSaving(val article: HeadlinesModel): NewsIntents
    data class ErrorOccurred(val  error: String): NewsIntents
    data class OpenHeadline(val headlineUrl: String): NewsIntents
    data class NavigateToScreen(val route: String): NewsIntents
    data object FetchNews : NewsIntents
    data object FetchSavedArticle : NewsIntents
}