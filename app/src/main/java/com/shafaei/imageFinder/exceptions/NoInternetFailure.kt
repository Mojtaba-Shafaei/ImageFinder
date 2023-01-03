package com.shafaei.imageFinder.exceptions

import com.shafaei.imageFinder.R.string

class NoInternetFailure : Failable {
  override val resId: Int
    get() = string.no_internet_connection
}