package com.shafaei.imageFinder.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

class AndroidUtil {
  companion object {
    private lateinit var context: Context
    private val schemeColors: IntArray by lazy {
      intArrayOf(
         0xff00ddff.toInt(),
         0xff99cc00.toInt(),
         0xffffbb33.toInt(),
         0xffff4444.toInt()
      )
    }

    fun init(context: Context) {
      this.context = context.applicationContext
    }

    fun dpToPx(dp: Int): Float {
      return TypedValue.applyDimension(
         TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
         context.resources.displayMetrics
      )
    }

    fun createProgressDrawable(large: Boolean = false): Drawable {
      return CircularProgressDrawable(context).apply {
        setColorSchemeColors(*schemeColors)
        setStyle(if (large) CircularProgressDrawable.LARGE else CircularProgressDrawable.DEFAULT)
        start()
      }
    }
  }
}