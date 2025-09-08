package com.herald.newsapp.presentation.screens.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.herald.newsapp.R

@Composable
fun LocalizationButton(
    onClick: () -> Unit
) {
    IconButton( onClick = { onClick() }){
        Icon(painter = painterResource(R.drawable.baseline_language_24), contentDescription = null)
    }
}