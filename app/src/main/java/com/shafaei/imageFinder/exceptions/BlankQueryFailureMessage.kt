package com.shafaei.imageFinder.exceptions

import com.shafaei.imageFinder.R.string

class BlankQueryFailureMessage : Failable {
  override val resId: Int
    get() = string.blank_query_exception

}