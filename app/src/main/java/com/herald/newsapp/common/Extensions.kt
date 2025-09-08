package com.herald.newsapp.common

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.herald.newsapp.R
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


fun Exception.toUserFriendlyMessage(context: Context): String {
    return when (this) {
        is HttpException -> context.getString(R.string.http_exception,this.code())
        is IOException -> context.getString(R.string.io_exception)
        else -> context.getString(R.string.unknown_error)
    }
}

fun String.getDateOnly(context: Context): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = inputFormat.parse(this) ?: return ""

    val now = Date()
    val diffMillis = now.time - date.time

    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when {
        seconds < 60 -> context.getString(R.string.relative_seconds, seconds)
        minutes < 60 -> context.getString(R.string.relative_minutes, minutes)
        hours < 24 -> context.getString(R.string.relative_hours, hours)
        days < 7 -> context.getString(R.string.relative_days, days)
        else -> context.getString(
            R.string.relative_date,
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        )
    }
}

suspend fun SnackbarHostState.showRetrySnackbar(
    actionLabel: String?,
    message: String,
    onActionClicked: (() -> Unit)?
) {
    val result = showSnackbar(
        message = message,
        actionLabel = actionLabel,
    )
    if (result == SnackbarResult.ActionPerformed) {
        if (onActionClicked != null) {
            onActionClicked()
        }
    }
}

fun Context.toggleLanguage( preferencesManager: PreferencesManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val localeManager = getSystemService(LocaleManager::class.java)
        val current = localeManager.applicationLocales.toLanguageTags()
        val newLang = if (current == "ar") "en" else "ar"
        localeManager.applicationLocales = LocaleList.forLanguageTags(newLang)
    } else {
        val current = preferencesManager.getString(LANGUAGE_KEY, "en")
        val newLang = if (current == "ar") "en" else "ar"
        preferencesManager.saveString(LANGUAGE_KEY,newLang)

        val updatedContext = setAppLocale(this, newLang)
        (this as? Activity)?.apply {
            applyOverrideConfiguration(updatedContext.resources.configuration)
            recreate()
        }
    }
}


private fun setAppLocale(context: Context, language: String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    return context.createConfigurationContext(config)
}