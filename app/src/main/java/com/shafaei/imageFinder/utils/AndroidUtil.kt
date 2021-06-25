package com.shafaei.imageFinder.utils

import android.content.Context
import android.util.TypedValue

class AndroidUtil {
  companion object {
    private lateinit var context: Context

    fun init(context: Context) {
      this.context = context.applicationContext
    }

    fun dpToPx(dp: Int): Float {
      return TypedValue.applyDimension(
         TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
         context.resources.displayMetrics
      )
    }
  }
}