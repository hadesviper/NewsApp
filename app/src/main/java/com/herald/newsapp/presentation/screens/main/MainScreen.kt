package com.herald.newsapp.presentation.screens.main

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.herald.newsapp.common.COUNTRY_KEY
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.presentation.actions.news.NewsEvents
import com.herald.newsapp.presentation.actions.news.NewsIntents
import com.herald.newsapp.presentation.viewmodels.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    navController: NavHostController,
    newsViewModel: NewsViewModel
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val bottomNavItems = listOf(
        Screens.NewsScreen,
        Screens.SearchScreen,
        Screens.SavedScreen
    )
    val context = LocalContext.current

    Column {
        Navigation(Modifier.weight(1f), navController, newsViewModel)
        NavigationBar {
            bottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = currentBackStackEntry.value?.destination?.route == item.route,
                    onClick = {
                        newsViewModel.handleIntent(NewsIntents.NavigateToScreen(item.route))
                    },
                    icon = { Icon(painterResource(item.icon), contentDescription = null) },
                    label = { Text(stringResource(item.label)) }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        newsViewModel.newsEvents.collectLatest {
            when (it) {
                is NewsEvents.NavigateToScreen -> navController.navigate(it.route)
                is NewsEvents.OpenHeadline -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.headlineUrl)))
                }
                is NewsEvents.ShowToast -> {
                    Toast.makeText(context, it.messageResID, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
private fun Navigation(
    modifier: Modifier,
    navController: NavHostController,
    newsViewModel: NewsViewModel
) {
    val newsScrollState = rememberLazyListState()
    val savedNewsScrollState = rememberLazyListState()
    val country = PreferencesManager(navController.context).getString(COUNTRY_KEY)

    NavHost(
        navController = navController,
        startDestination = Screens.NewsScreen.route,
        modifier = modifier
    ) {
        composable(route = Screens.NewsScreen.route) {
            NewsScreen(country, newsScrollState, newsViewModel)
        }
        composable(route = Screens.SearchScreen.route) {
            Text(Screens.SearchScreen.route)
        }
        composable(route = Screens.SavedScreen.route) {
            SavedArticlesScreen(savedNewsScrollState, newsViewModel)
        }
    }
}