package com.shafaei.imageFinder.exceptions

import android.content.Context
import androidx.annotation.StringRes

interface Failable {
  @get:StringRes
  val resId: Int

  val extraMessage: String?
    get() = null

  fun asString(context: Context): String {
    return context.getString(resId) + if (extraMessage.isNullOrBlank()) "" else ":$extraMessage"
  }
}