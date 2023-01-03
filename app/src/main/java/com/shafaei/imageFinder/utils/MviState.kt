package com.shafaei.imageFinder.utils

import com.shafaei.imageFinder.exceptions.Failable
import com.shafaei.imageFinder.exceptions.UnhandledFailure

// Lce -> Loading / Content / Error
data class MviState<T>(
   val isLoading: Boolean,
   val error: Failable?,
   val data: T?,
) {

  fun hasError(): Boolean = (error != null)
  val isIdle: Boolean
    get() = (!isLoading && error == null && data == null)

  companion object {
    fun <E> data(data: E) = MviState(isLoading = false, error = null, data = data)
    fun <E>error(error: Failable = UnhandledFailure()) = MviState<E>(isLoading = false, error = error, data = null)
    fun <E>loading() = MviState<E>(isLoading = true, error = null, data = null)
    fun <E>idle() = MviState<E>(isLoading = false, error = null, data = null)
  }
}