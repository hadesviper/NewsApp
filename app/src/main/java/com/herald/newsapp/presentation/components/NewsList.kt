package com.herald.newsapp.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.herald.newsapp.domain.models.HeadlinesModel

@Composable
fun NewsList(
    newsItems: List<HeadlinesModel>,
    lazyListState: LazyListState,
    onSaveClick: (HeadlinesModel) -> Unit,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        state = lazyListState,
    ) {
        items(items = newsItems,
            key = { it.url },
            contentType = { HeadlinesModel::class.java }
        ) { item ->
            NewsItem(item, { onSaveClick(item) }, { onItemClick(item.url) })
        }
    }
}