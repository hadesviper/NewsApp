package com.herald.newsapp.common

import android.content.Context
import com.herald.newsapp.domain.ResourceProvider
import javax.inject.Inject

class StringsProvider @Inject constructor(
    private val context: Context
) : ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)
}