package com.herald.newsapp.presentation.actions.news

interface NewsEvents {
    data class ErrorOccurred(val exception: Exception): NewsEvents
    data class OpenHeadline(val headlineUrl: String): NewsEvents
    data class NavigateToScreen(val route: String): NewsEvents
    data class ShowToast(val messageResID: Int): NewsEvents
}