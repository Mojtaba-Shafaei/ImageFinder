package com.shafaei.imageFinder.exceptions

import android.content.Context
import androidx.annotation.StringRes
import com.shafaei.imageFinder.R


sealed class MyException(@StringRes val resId: Int, val extraMessage: String? = null) {
  fun string(context: Context): String {
    return context.getString(resId)
  }
}

class NoInternetException : MyException(R.string.no_internet_connection)
class CanNotGetMoreResultException : MyException(R.string.you_cannot_search_more)
class UnhandledException(extraMessage: String? = null) : MyException(R.string.unhandled_error, extraMessage)
class BlankQueryException : MyException(R.string.blank_query_exception)
data class ShortQueryException(val len: Int) : MyException(R.string.short_query_exception)
class MissedPixabayKeyException : MyException(R.string.missedPixabayKeyException)