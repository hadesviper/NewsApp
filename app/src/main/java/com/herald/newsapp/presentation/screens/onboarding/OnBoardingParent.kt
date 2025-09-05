package com.herald.newsapp.presentation.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.presentation.actions.onboarding.OnBoardingIntents
import com.herald.newsapp.presentation.components.LocalizationButton
import com.herald.newsapp.presentation.viewmodels.OnBoardingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingParent(
    title:String,
    onBoardingViewModel: OnBoardingViewModel,
    content: @Composable () -> Unit){
    val currentPage by onBoardingViewModel.currentPage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    if (currentPage > 0) {
                        IconButton(onClick = {
                            onBoardingViewModel.handleIntent(OnBoardingIntents.PreviousPage)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                actions = {
                    LocalizationButton(onClick = { onBoardingViewModel.handleIntent(OnBoardingIntents.SwitchLanguage(context,PreferencesManager(context))) })
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}