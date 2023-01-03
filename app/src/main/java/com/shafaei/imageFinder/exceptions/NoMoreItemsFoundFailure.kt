package com.shafaei.imageFinder.exceptions

import com.shafaei.imageFinder.R

class NoMoreItemsFoundFailure : Failable {
  override val resId: Int
    get() = R.string.you_cannot_search_more
}
