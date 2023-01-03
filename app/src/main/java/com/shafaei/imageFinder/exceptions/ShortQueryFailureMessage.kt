package com.shafaei.imageFinder.exceptions

import com.shafaei.imageFinder.R

data class ShortQueryFailureMessage(val len: Int) : Failable {
  override val resId: Int
    get() = R.string.short_query_exception
}