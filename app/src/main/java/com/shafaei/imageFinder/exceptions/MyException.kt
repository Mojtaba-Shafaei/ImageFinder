package com.shafaei.imageFinder.exceptions

import android.content.Context
import androidx.annotation.StringRes
import com.shafaei.imageFinder.R


sealed class MyException(@StringRes val resId: Int) {
  fun string(context: Context): String {
    return context.getString(resId)
  }
}

class NoInternetException : MyException(R.string.no_internet_connection)
class CanNotGetMoreResultException : MyException(R.string.you_cannot_search_more)
class UnhandledException : MyException(R.string.unhandled_error)