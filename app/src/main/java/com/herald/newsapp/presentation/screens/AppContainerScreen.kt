package com.herald.newsapp.presentation.screens

import android.content.Context
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.herald.newsapp.common.COUNTRY_KEY
import com.herald.newsapp.common.IS_INTRO_FINISHED
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.presentation.actions.onboarding.OnBoardingEvents
import com.herald.newsapp.presentation.screens.main.MainNavScreen
import com.herald.newsapp.presentation.screens.onboarding.ChooseCategoriesScreen
import com.herald.newsapp.presentation.screens.onboarding.ChooseCountryScreen
import com.herald.newsapp.presentation.viewmodels.NewsViewModel
import com.herald.newsapp.presentation.viewmodels.OnBoardingViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ResponsiveLayout() {
    BoxWithConstraints {
        if (maxWidth < maxHeight) {
            // Portrait UI
            Column {
                Text("Portrait UI")
            }
        } else {
            // Landscape UI
            Row {
                Text("Landscape UI")
            }
        }
    }
}
@Composable
fun AppContainer(
    navController: NavHostController,
    onBoardingViewModel: OnBoardingViewModel,
    newsViewModel: NewsViewModel
) {
    val preferencesManager = PreferencesManager(navController.context)
    if (preferencesManager.getBoolean(IS_INTRO_FINISHED)) {
        MainNavScreen(navController, newsViewModel)
        return
    }

    val currentPage by onBoardingViewModel.currentPage.collectAsStateWithLifecycle()

    when (currentPage) {
        0 -> ChooseCountryScreen(onBoardingViewModel)
        1 -> ChooseCategoriesScreen(onBoardingViewModel)
        2 -> {
            preferencesManager.saveBoolean(IS_INTRO_FINISHED, true)
            MainNavScreen(navController, newsViewModel)
        }
    }
    LaunchedEffect(Unit) {
        onBoardingViewModel.onBoardingEvents.collectLatest {
            when (it) {
                is OnBoardingEvents.SaveCountry -> saveCountry(navController.context, it.country)
            }
        }
    }
}

private fun saveCountry(context: Context, country: String){
    PreferencesManager(context).saveString(COUNTRY_KEY, country)
}

