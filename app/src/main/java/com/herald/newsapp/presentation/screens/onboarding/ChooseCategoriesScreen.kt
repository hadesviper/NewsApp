package com.herald.newsapp.presentation.screens.onboarding

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.herald.newsapp.R
import com.herald.newsapp.common.CATEGORIES_MAX_SIZE
import com.herald.newsapp.presentation.actions.onboarding.OnBoardingIntents
import com.herald.newsapp.presentation.viewmodels.OnBoardingViewModel

@Composable
fun ChooseCategoriesScreen(
    onBoardingViewModel: OnBoardingViewModel,
) {
    OnBoardingParent(stringResource(R.string.choose_categories),onBoardingViewModel) {
        val categories = stringArrayResource(R.array.categories)
        val categoriesEn = stringArrayResource(R.array.categories_en)
        var selectedCategories by remember { mutableStateOf(emptyList<String>()) }
        Text(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            text = stringResource(R.string.choose_three_categories),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        FlowRow(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            categories.forEachIndexed { index, category ->
                val isSelected = categoriesEn[index] in selectedCategories
                FilterChip(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    selected = isSelected,
                    onClick = {
                        selectedCategories = if (isSelected) {
                            selectedCategories - categoriesEn[index]
                        } else {
                            selectedCategories + categoriesEn[index]
                        }
                        if (selectedCategories.count() == CATEGORIES_MAX_SIZE)
                            onBoardingViewModel.handleIntent(OnBoardingIntents.SaveCategories(selectedCategories.toList()))
                    },
                    label = { Text(text = category) }
                )
            }
        }
    }
}