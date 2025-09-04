package com.herald.newsapp.presentation.components

import com.herald.newsapp.R
import com.herald.newsapp.common.HOME_SCREEN
import com.herald.newsapp.common.SAVED_SCREEN
import com.herald.newsapp.common.SEARCH_SCREEN


sealed class Screens(val route: String, val label: Int, val icon: Int) {
    data object NewsScreen :    Screens(HOME_SCREEN,R.string.home, R.drawable.baseline_home_24)
    data object SearchScreen :  Screens(SEARCH_SCREEN,R.string.search, R.drawable.baseline_search_24)
    data object SavedScreen :   Screens(SAVED_SCREEN,R.string.saved, R.drawable.baseline_bookmarks_24)
}


