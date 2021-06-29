package com.shafaei.imageFinder.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

object AndroidUtil {

  private val schemeColors: IntArray by lazy {
    intArrayOf(
       0xff00ddff.toInt(),
       0xff99cc00.toInt(),
       0xffffbb33.toInt(),
       0xffff4444.toInt()
    )
  }

  fun createProgressDrawable(context: Context, large: Boolean = false): Drawable {
    return CircularProgressDrawable(context).apply {
      setColorSchemeColors(*schemeColors)
      setStyle(if (large) CircularProgressDrawable.LARGE else CircularProgressDrawable.DEFAULT)
      start()
    }
  }
}
