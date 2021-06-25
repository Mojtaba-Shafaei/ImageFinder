package com.shafaei.imageFinder

import android.app.Application
import com.shafaei.imageFinder.utils.AndroidUtil

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    AndroidUtil.init(this)
  }
}