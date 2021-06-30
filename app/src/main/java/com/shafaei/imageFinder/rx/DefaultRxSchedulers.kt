package com.shafaei.imageFinder.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefaultRxSchedulers @Inject constructor() : SchedulerProvider {
  override fun ui(): Scheduler = AndroidSchedulers.mainThread()

  override fun io(): Scheduler = Schedulers.io()
}