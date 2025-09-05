package com.herald.newsapp.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.newsapp.R
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.common.Resource
import com.herald.newsapp.common.toggleLanguage
import com.herald.newsapp.domain.local.categories.usecases.GetCategoriesUseCase
import com.herald.newsapp.domain.models.HeadlinesModel
import com.herald.newsapp.domain.models.LocalUseCasesModel
import com.herald.newsapp.domain.remote.usecases.FetchNewsUseCase
import com.herald.newsapp.presentation.actions.news.NewsEvents
import com.herald.newsapp.presentation.actions.news.NewsIntents
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
    private val localUseCases: LocalUseCasesModel,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _newsState = MutableStateFlow(NewsState())
    val newsState = _newsState.asStateFlow()

    private val _savedArticlesState = MutableStateFlow<List<HeadlinesModel>>(emptyList())
    val savedArticlesState = _savedArticlesState.asStateFlow()

    private val _newsEvents = MutableSharedFlow<NewsEvents>()
    val newsEvents = _newsEvents.asSharedFlow()

    private var selectedCategories: List<String> = emptyList()

    init {
        fetchSavedArticles()
    }

    fun handleIntent(intent: NewsIntents) {
        when (intent) {
            is NewsIntents.FetchNews -> fetchNews(intent.country)
            is NewsIntents.FetchSavedArticle -> fetchSavedArticles()
            is NewsIntents.OnSearchQueryChanged -> TODO()
            is NewsIntents.NavigateToScreen -> triggerEvent(NewsEvents.NavigateToScreen(intent.route))
            is NewsIntents.ArticleSaving -> articleSaving(intent.article)
            is NewsIntents.ErrorOccurred -> triggerEvent(NewsEvents.ErrorOccurred(intent.exception))
            is NewsIntents.OpenHeadline -> triggerEvent(NewsEvents.OpenHeadline(intent.headlineUrl))
            is NewsIntents.SwitchLanguage -> switchLanguage(intent.context, intent.preferencesManager)
        }
    }

    private fun fetchNews(country: String) = viewModelScope.launch(Dispatchers.IO) {
        if (selectedCategories.isEmpty()) selectedCategories = getCategoriesUseCase()
        fetchNewsUseCase(country, selectedCategories).collect { result ->
            when (result) {
                is Resource.Loading -> _newsState.update { newsState -> newsState.copy(isLoading = true, exception = null) }
                is Resource.Success -> remoteDataFetchingSuccess(result.data)
                is Resource.Error -> remoteDataFetchingError(result.exception)
            }
        }
    }

    private fun remoteDataFetchingSuccess(news: List<HeadlinesModel>) = viewModelScope.launch(Dispatchers.IO){
        localUseCases.deleteCachedNewsUseCase()
        localUseCases.addCachedNewsUseCase(news)
        startCollectingCachedNews()
    }

    private fun remoteDataFetchingError(exception: Exception) = viewModelScope.launch(Dispatchers.IO) {
        _newsState.update { newsState -> newsState.copy(isLoading = false, exception = exception) }
        triggerEvent(NewsEvents.ErrorOccurred(exception))
        startCollectingCachedNews()
    }

    private fun articleSaving(article: HeadlinesModel) = viewModelScope.launch(Dispatchers.IO) {
        localUseCases.articleSavingUseCase(article)
        localUseCases.updateCachedUseCase(article.url, !article.isSaved)
        triggerEvent(NewsEvents.ShowToast(if (article.isSaved) R.string.article_removed else R.string.article_saved))
    }

    private fun fetchSavedArticles() = viewModelScope.launch(Dispatchers.IO) {
        localUseCases.getSavedArticlesUseCase().collect { result ->
            _savedArticlesState.update { result }
        }
    }

    private fun startCollectingCachedNews() = viewModelScope.launch (Dispatchers.IO){
        localUseCases.fetchCachedNewsUseCase().collect { cachedNews ->
            _newsState.update { newsState -> newsState.copy(isLoading = false, news = cachedNews) }
        }
    }

    private fun switchLanguage(context: Context, preferencesManager: PreferencesManager) {
        context.toggleLanguage(preferencesManager)
    }

    private fun triggerEvent(event: NewsEvents) = viewModelScope.launch(Dispatchers.IO) {
        _newsEvents.emit(event)
    }
}