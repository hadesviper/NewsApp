package com.herald.newsapp.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.common.toggleLanguage
import com.herald.newsapp.domain.local.categories.usecases.SaveCategoriesUseCase
import com.herald.newsapp.presentation.actions.onboarding.OnBoardingEvents
import com.herald.newsapp.presentation.actions.onboarding.OnBoardingIntents
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
class OnBoardingViewModel @Inject constructor(
    private val saveCategoriesUseCase: SaveCategoriesUseCase
): ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage = _currentPage.asStateFlow()
    private val _onBoardingEvents = MutableSharedFlow<OnBoardingEvents>()
    val onBoardingEvents = _onBoardingEvents.asSharedFlow()

    fun handleIntent(intent: OnBoardingIntents) {
        when(intent){
            is OnBoardingIntents.SaveCategories -> saveCategories(intent.categories)
            is OnBoardingIntents.NextPage -> _currentPage.update { it + 1 }
            is OnBoardingIntents.PreviousPage -> _currentPage.update { it - 1 }
            is OnBoardingIntents.SaveCountry -> emitEvent(OnBoardingEvents.SaveCountry(intent.country))
            is OnBoardingIntents.SwitchLanguage -> switchLanguage(intent.context,intent.preferencesManager)
        }
    }

    private fun saveCategories(categories: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        saveCategoriesUseCase(categories)
        _currentPage.update { 2 }
    }

    private fun switchLanguage(context: Context, preferencesManager: PreferencesManager) {
        context.toggleLanguage(preferencesManager)
    }

    private fun emitEvent(event: OnBoardingEvents) = viewModelScope.launch(Dispatchers.IO) {
        _onBoardingEvents.emit(event)
    }
}