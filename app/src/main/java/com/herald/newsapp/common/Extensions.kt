package com.herald.newsapp.common

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.herald.newsapp.R
import com.herald.newsapp.domain.ResourceProvider
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


fun Exception.toUserFriendlyMessage(resourceProvider: ResourceProvider): String {
    return when (this) {
        is HttpException -> resourceProvider.getString(R.string.http_exception)
        is IOException -> resourceProvider.getString(R.string.io_exception)
        else -> this.message ?: resourceProvider.getString(R.string.unknown_error)
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
    actionLabel: String,
    message: String,
    onActionClicked: () -> Unit
) {
    val result = showSnackbar(
        message = message,
        actionLabel = actionLabel,
    )
    if (result == SnackbarResult.ActionPerformed) { onActionClicked() }
}
