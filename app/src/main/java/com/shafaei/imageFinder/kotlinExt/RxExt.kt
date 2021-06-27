package com.shafaei.imageFinder.kotlinExt

import com.shafaei.imageFinder.utils.Lce
import com.shafaei.imageFinder.utils.Result
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import kotlin.math.pow

fun <T> Single<T>.applyRetry3(): Single<T> = this.retryWhen { errors ->
  errors.map { 1 }
     .scan { t1, t2 -> t1 + t2 }
     .doOnNext { errorCount -> println("No. of errors: $errorCount") }
     .takeWhile { errorCount -> errorCount < 3 }
     .flatMapSingle { errorCount -> Single.timer(3.0.pow(errorCount).toLong(), TimeUnit.SECONDS) }
}

fun <T> Single<Result<T>>.mapToLce(): Single<Lce<T>> {
  return this.map { result ->
    if (result is Result.Success) {
      Lce.data<T>(result.data)
    } else {
      Lce.error<T>((result as Result.Failure).exception)
    }
  }
}

fun <T> Observable<Result<T>>.mapToLce(): Observable<Lce<T>> {
  return this.map { result ->
    if (result is Result.Success) {
      Lce.data<T>(result.data)
    } else {
      Lce.error<T>((result as Result.Failure).exception)
    }
  }
}

fun <T> Observable<Lce<T>>.onErrorResumeNextLceFailure(action: (() -> Lce<T>)? = null): Observable<Lce<T>> {
  return this.compose { upstream ->
    upstream.onErrorResumeNext { t: Throwable ->
      Observable.just(action?.invoke() ?: Lce.error<T>(t.mapToMyException()))
    }
  }
}