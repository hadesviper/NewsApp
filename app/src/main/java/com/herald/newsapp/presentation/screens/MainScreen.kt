package com.herald.newsapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.herald.newsapp.presentation.NewsViewModel
import com.herald.newsapp.presentation.actions.NewsEvents
import com.herald.newsapp.presentation.actions.NewsIntents
import com.herald.newsapp.presentation.components.Screens
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(navController: NavHostController, newsViewModel: NewsViewModel) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val bottomNavItems = listOf(
        Screens.NewsScreen,
        Screens.SearchScreen,
        Screens.SavedScreen
    )

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
    NavHost(
        navController = navController,
        startDestination = Screens.NewsScreen.route,
        modifier = modifier
    ) {
        composable(route = Screens.NewsScreen.route) {
            NewsScreen(newsViewModel)
        }
        composable(route = Screens.SearchScreen.route) {
            Text(Screens.SearchScreen.route)
        }
        composable(route = Screens.SavedScreen.route) {
            Text(Screens.SavedScreen.route)
        }
    }
}