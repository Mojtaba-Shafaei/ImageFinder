package com.shafaei.imageFinder.utils

import com.shafaei.imageFinder.exceptions.MyException
import com.shafaei.imageFinder.exceptions.UnhandledException

// Lce -> Loading / Content / Error
abstract class Lce<T> : Comparable<Lce<T>> {

  abstract val isLoading: Boolean
  abstract val error: MyException?
  abstract val data: T?

  fun hasError(): Boolean = (error != null)

  companion object {
    fun <E> data(data: E): Lce<E> {
      return object : Lce<E>() {
        override val isLoading: Boolean
          get() = false

        override val error: MyException?
          get() = null

        override val data: E
          get() = data
      }
    }

    fun <E> error(error: MyException = UnhandledException()): Lce<E> {
      return object : Lce<E>() {

        override val isLoading: Boolean
          get() = false

        override val error: MyException
          get() = error

        override val data: E?
          get() = null
      }
    }

    fun <E> loading(percent: Double? = null): Lce<E> {
      return object : Lce<E>() {

        override val isLoading: Boolean
          get() = true

        override val error: MyException?
          get() = null

        override val data: E?
          get() = null
      }
    }
  }

  override fun compareTo(other: Lce<T>): Int {
    if ((hasError() == other.hasError() && error == other.error) && (data == other.data) && (isLoading == other.isLoading)) return 0

    return 1
  }
}