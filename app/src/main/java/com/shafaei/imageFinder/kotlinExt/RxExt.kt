package com.shafaei.imageFinder.kotlinExt

import com.shafaei.imageFinder.utils.Lce
import com.shafaei.imageFinder.utils.Result
import io.reactivex.Observable
import io.reactivex.Single

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