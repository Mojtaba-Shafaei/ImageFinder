package com.shafaei.imageFinder.utils

import com.shafaei.imageFinder.exceptions.MyException

sealed interface Lce<T> {
  interface Loading<T> : Lce<T>
  data class Data<T>(val items: List<T>)
  data class Error<T>(val error: MyException)
}
