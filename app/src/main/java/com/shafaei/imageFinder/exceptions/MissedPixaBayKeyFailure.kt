package com.shafaei.imageFinder.exceptions

import com.shafaei.imageFinder.R


class MissedPixaBayKeyFailure : Failable {
  override val resId: Int
    get() = R.string.missedPixabayKeyException
}