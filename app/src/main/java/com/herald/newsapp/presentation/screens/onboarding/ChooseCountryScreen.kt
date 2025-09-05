package com.herald.newsapp.presentation.screens.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.herald.newsapp.R
import com.herald.newsapp.common.COUNTRY_KEY
import com.herald.newsapp.common.PreferencesManager
import com.herald.newsapp.presentation.actions.onboarding.OnBoardingEvents
import com.herald.newsapp.presentation.actions.onboarding.OnBoardingIntents
import com.herald.newsapp.presentation.viewmodels.OnBoardingViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
fun ChooseCountryScreen(
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    OnBoardingParent(stringResource(R.string.choose_country), onBoardingViewModel) {
        val countries = getLocalizedCountries()
        val onCountrySelected = { countryCode: String ->
            onBoardingViewModel.handleIntent(OnBoardingIntents.SaveCountry(countryCode))
        }
        WarningText()
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(countries) { (code, name) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_small))
                        .clickable { onCountrySelected(code) },
                    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.padding_small))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    ) {
                        Text(text = name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        onBoardingViewModel.onBoardingEvents.collectLatest {
            when (it) {
                is OnBoardingEvents.SaveCountry -> {
                    PreferencesManager(context).saveString(COUNTRY_KEY, it.country)
                    onBoardingViewModel.handleIntent(OnBoardingIntents.NextPage)
                }
            }
        }
    }
}

@Composable
private fun WarningText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))

    ) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        Text(stringResource(R.string.api_supports_us),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

private fun getLocalizedCountries(locale: Locale = Locale.getDefault()): List<Pair<String, String>> {
    val allCountries = Locale.getISOCountries().map { countryCode ->
        val countryLocale = Locale("", countryCode)
        val name = countryLocale.getDisplayCountry(locale)
        countryCode to name
    }
    val us = allCountries.firstOrNull { it.first == "US" }
    val others = allCountries.filterNot { it.first == "US" }.sortedBy { it.second }
    return listOfNotNull(us) + others
}
