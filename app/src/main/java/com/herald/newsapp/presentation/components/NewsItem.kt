package com.herald.newsapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.herald.newsapp.R
import com.herald.newsapp.common.getDateOnly
import com.herald.newsapp.domain.models.HeadlinesModel

@Composable
fun NewsItem(
    newsItem: HeadlinesModel,
    onSaveClick: (HeadlinesModel) -> Unit,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.
        clickable { onItemClick(newsItem.url)}.
        padding(dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        HeadlineImage(newsItem){ onSaveClick(newsItem) }
        NewsData(newsItem)
    }
}

@Composable
private fun HeadlineImage(newsItem: HeadlinesModel,onSaveClick: (HeadlinesModel) -> Unit) {
    Box {
        AsyncImage(
            model = newsItem.image,
            contentDescription = newsItem.title,
            contentScale = ContentScale.FillWidth,
            placeholder = painterResource(R.drawable.image_loading),
            error = painterResource(R.drawable.no_image),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius)))
        )
        IconButton(onClick = { onSaveClick(newsItem) },modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(painter = painterResource(if (newsItem.isSaved) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun NewsData(newsItem: HeadlinesModel) {
    Text(
        text = newsItem.title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )

    DateAndSource(newsItem.date, newsItem.sourceNewspaper)

    Text(
        text = newsItem.shortDescription,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun DateAndSource(date: String, sourceNewspaper: String) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = date.getDateOnly(context),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = sourceNewspaper,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}