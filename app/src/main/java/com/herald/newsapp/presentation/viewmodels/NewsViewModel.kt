package com.herald.newsapp.presentation.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.newsapp.R
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.common.Resource
import com.herald.newsapp.common.toggleLanguage
import com.herald.newsapp.domain.local.categories.usecases.GetCategoriesUseCase
import com.herald.newsapp.domain.models.HeadlinesModel
import com.herald.newsapp.domain.models.LocalUseCasesModel
import com.herald.newsapp.domain.models.SearchQuery
import com.herald.newsapp.domain.remote.usecases.FetchNewsUseCase
import com.herald.newsapp.domain.remote.usecases.SearchNewsUseCase
import com.herald.newsapp.presentation.actions.news.NewsEvents
import com.herald.newsapp.presentation.actions.news.NewsIntents
import com.herald.newsapp.presentation.states.NewsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class NewsViewModel @Inject constructor(
    private val fetchNewsUseCase: FetchNewsUseCase,
    private val localUseCases: LocalUseCasesModel,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _newsState = MutableStateFlow(NewsState())
    val newsState = _newsState.asStateFlow()

    private val _newsSearchingState = MutableStateFlow(NewsState())
    val newsSearchingState = _newsSearchingState.asStateFlow()

    private val _savedArticlesState = MutableStateFlow<List<HeadlinesModel>>(emptyList())
    val savedArticlesState = _savedArticlesState.asStateFlow()

    private val _newsEvents = MutableSharedFlow<NewsEvents>()
    val newsEvents = _newsEvents.asSharedFlow()

    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())
    val selectedCategories = _selectedCategories.asStateFlow()

    private val _searchQueryState = MutableStateFlow(SearchQuery())
    private val searchResult = _searchQueryState
        .debounce(1000)
        .distinctUntilChanged()
        .filter { it.query.isNotEmpty()}
        .flatMapLatest { query ->
            searchNewsUseCase(query)
        }

    init {
        initializeSearchResultFlow()
        fetchSavedArticles()
    }

    fun handleIntent(intent: NewsIntents) {
        when (intent) {
            is NewsIntents.FetchNews -> fetchNews(intent.country)
            is NewsIntents.OnSearchQueryChanged -> onSearchQueryChanged(intent.searchQuery)
            is NewsIntents.NavigateToScreen -> triggerEvent(NewsEvents.NavigateToScreen(intent.route))
            is NewsIntents.ArticleSaving -> articleSaving(intent.article,intent.isSearchResult)
            is NewsIntents.ErrorOccurred -> triggerEvent(NewsEvents.ErrorOccurred(intent.exception))
            is NewsIntents.OpenHeadline -> openArticleInBrowser(intent.headlineUrl, intent.context)
            is NewsIntents.SwitchLanguage -> switchLanguage(intent.context, intent.preferencesManager)
        }
    }

    private fun fetchNews(country: String) = viewModelScope.launch(Dispatchers.IO) {
        if (_selectedCategories.value.isEmpty()) initializeSelectedCategories()
        fetchNewsUseCase(country, _selectedCategories.value).collect { result ->
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
        startCollectingCachedNews()
    }

    private fun articleSaving(article: HeadlinesModel, isSearchResult: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        localUseCases.articleSavingUseCase(article)
        localUseCases.updateCachedUseCase(article.url, !article.isSaved)
        if (isSearchResult) updateSearchResultSavedState(article)
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

    private suspend fun initializeSelectedCategories() {
        _selectedCategories.update { getCategoriesUseCase() }
    }

    private fun initializeSearchResultFlow() = viewModelScope.launch(Dispatchers.IO) {
        searchResult.collectLatest { result ->
            when (result) {
                is Resource.Loading -> _newsSearchingState.update { newsState -> newsState.copy(isLoading = true, exception = null) }
                is Resource.Success -> _newsSearchingState.update {
                    newsState -> newsState.copy(isLoading = false, news = markSavedArticles(result.data))
                }
                is Resource.Error -> _newsSearchingState.update { newsState -> newsState.copy(isLoading = false, exception = result.exception) }
            }
        }
    }

    private fun onSearchQueryChanged(searchQuery: SearchQuery) = viewModelScope.launch {
        _searchQueryState.update { searchQuery }
    }

    private fun markSavedArticles(data: List<HeadlinesModel>): List<HeadlinesModel> {
        val savedUrls = _savedArticlesState.value.map { it.url }.toSet()
        return data.map { article ->
            article.copy(isSaved = article.url in savedUrls)
        }
    }
    private fun updateSearchResultSavedState(article: HeadlinesModel) {
        _newsSearchingState.update { newsState ->
            val index = newsState.news?.indexOfFirst { it.url == article.url } ?: -1
            if (index == -1) return
            val updatedList = newsState.news?.toMutableList()?.apply {
                this[index] = this[index].copy(isSaved = !article.isSaved)
            }
            newsState.copy(news = updatedList)
        }
    }

    private fun openArticleInBrowser(headlineUrl: String, context: Context) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(headlineUrl)))
    }

    private fun switchLanguage(context: Context, preferencesManager: PreferencesManager) {
        context.toggleLanguage(preferencesManager)
    }

    private fun triggerEvent(event: NewsEvents) = viewModelScope.launch(Dispatchers.Main) {
        _newsEvents.emit(event)
    }
}
