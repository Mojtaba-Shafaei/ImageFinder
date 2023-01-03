package com.shafaei.imageFinder.utils

import com.shafaei.imageFinder.exceptions.Failable

sealed interface Result<T> {
  data class Success<T>(val data: T) : Result<T>
  data class Failure<T>(val failure: Failable) : Result<T>

  val isSuccess: Boolean
    get() = this is Success

  fun getOrNull(): T? {
    return if (this is Success) {
      this.data
    } else {
      null
    }
  }

  val failOrNull: Failable?
    get() {
      return if (this is Failure) {
        this.failure
      } else {
        null
      }
    }

  val failable: Failable
    get() {
      return (this as Failure).failure
    }
}
