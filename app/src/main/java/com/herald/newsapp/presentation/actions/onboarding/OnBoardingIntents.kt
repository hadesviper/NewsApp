package com.herald.newsapp.presentation.actions.onboarding

import android.content.Context
import com.herald.newsapp.common.PreferencesManager

sealed interface OnBoardingIntents {
    data object NextPage: OnBoardingIntents
    data object PreviousPage: OnBoardingIntents
    data class SaveCountry(val country: String): OnBoardingIntents
    data class SaveCategories(val categories: List<String>): OnBoardingIntents
    data class SwitchLanguage(val context: Context,val preferencesManager: PreferencesManager): OnBoardingIntents
}