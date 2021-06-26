package com.shafaei.imageFinder.utils

import com.shafaei.imageFinder.exceptions.MyException

sealed interface Result<T> {
  data class Success<T>(val data: T) : Result<T>
  data class Failure<T>(val exception: MyException) : Result<T>
}
