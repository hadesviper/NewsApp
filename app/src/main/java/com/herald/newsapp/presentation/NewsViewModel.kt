package com.herald.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.newsapp.common.Resource
import com.herald.newsapp.common.toUserFriendlyMessage
import com.herald.newsapp.domain.ResourceProvider
import com.herald.newsapp.domain.remote.usecases.FetchNewsUseCase
import com.herald.newsapp.domain.remote.usecases.SearchNewsUseCase
import com.herald.newsapp.presentation.actions.NewsEvents
import com.herald.newsapp.presentation.actions.NewsIntents
import com.herald.newsapp.presentation.states.NewsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val fetchNewsUseCase: FetchNewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val resourceProvider: ResourceProvider
): ViewModel() {

    private val _newsState = MutableStateFlow(NewsState())
    val newsState = _newsState.asStateFlow()

    private val _newsEvents = MutableSharedFlow<NewsEvents>()
    val newsEvents = _newsEvents.asSharedFlow()

    fun handleIntent(intent: NewsIntents) {
        when (intent) {
            is NewsIntents.FetchNews -> fetchNews()
            is NewsIntents.OnSearchQueryChanged -> TODO()
            is NewsIntents.NavigateToScreen -> triggerEvent(NewsEvents.NavigateToScreen(intent.route))
            is NewsIntents.SaveHeadline -> TODO()
            is NewsIntents.ErrorOccurred -> triggerEvent(NewsEvents.ErrorOccurred(intent.error))
            is NewsIntents.OpenHeadline -> triggerEvent(NewsEvents.OpenHeadline(intent.headlineUrl))
        }
    }

    private fun fetchNews() = viewModelScope.launch(Dispatchers.IO) {
        fetchNewsUseCase("us", listOf("general")).collect { result->
            when (result) {
                is Resource.Loading -> _newsState.update { newsState -> newsState.copy(isLoading = true, error = null) }
                is Resource.Success -> _newsState.update { NewsState(news = result.data) }
                is Resource.Error -> {
                    result.exception.toUserFriendlyMessage(resourceProvider).let { message ->
                        triggerEvent(NewsEvents.ErrorOccurred(message))
                        _newsState.update { newsState -> newsState.copy(isLoading = false, error = message) }
                    }
                }
            }
        }
    }

    private fun triggerEvent(event: NewsEvents) = viewModelScope.launch(Dispatchers.IO) {
        _newsEvents.emit(event)
    }
}