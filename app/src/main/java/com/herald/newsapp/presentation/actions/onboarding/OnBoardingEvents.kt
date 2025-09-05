package com.herald.newsapp.presentation.actions.onboarding

sealed interface OnBoardingEvents {
    data class SaveCountry(val country: String): OnBoardingEvents
}