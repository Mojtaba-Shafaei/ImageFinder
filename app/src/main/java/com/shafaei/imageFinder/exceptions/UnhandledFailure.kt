package com.shafaei.imageFinder.exceptions

import com.shafaei.imageFinder.R.string

data class UnhandledFailure(val msg: String? = null) : Failable {
  override val resId: Int
    get() = string.unhandled_error

  override val extraMessage: String?
    get() = msg
}