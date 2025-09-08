package com.herald.newsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.herald.newsapp.presentation.screens.AppContainer
import com.herald.newsapp.presentation.screens.common.ui.theme.NewsAppTheme
import com.herald.newsapp.presentation.viewmodels.NewsViewModel
import com.herald.newsapp.presentation.viewmodels.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                AppContainer(navController, onBoardingViewModel, newsViewModel)
            }
        }
    }
}
