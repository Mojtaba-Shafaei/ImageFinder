package com.shafaei.imageFinder.utils

import io.reactivex.Single
import java.util.concurrent.TimeUnit.MILLISECONDS

object RxUtils {
  fun <T> singleDelay(delay: Long = 200, value: T): Single<T> {
    return Single.timer(delay, MILLISECONDS).map { value }
  }
}