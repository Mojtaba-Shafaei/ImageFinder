package com.shafaei.imageFinder

import android.app.Application
import com.shafaei.imageFinder.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@App)
      modules(appModules)
    }
  }
}