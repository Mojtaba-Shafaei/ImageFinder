package com.shafaei.imageFinder.kotlinExt

import com.shafaei.imageFinder.exceptions.*
import com.shafaei.imageFinder.utils.MviState
import com.shafaei.imageFinder.utils.Result.Failure
import com.shafaei.imageFinder.utils.Result as Result1


fun Throwable.asFailableOrNull(): Failable? {
  return ExceptionMapper.mapOrNull(this)
}

fun Throwable.asFailable(): Failable {
  return (this.asFailableOrNull() ?: UnhandledFailure())
}

fun <T> Throwable.asFailure(): Failure<T> {
  return Result1.Failure(this.asFailable())
}

fun <T> Throwable.asErrorState(): MviState<T> {
  return MviState.error(this.asFailable())
}