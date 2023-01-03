package com.shafaei.imageFinder.exceptions

import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExceptionMapper {
  fun mapOrNull(t: Throwable?): Failable? {
    if (t == null) return null

    return when (t) {
      is UnknownHostException, is SocketTimeoutException -> NoInternetFailure()
      else -> mapOrNull(t.message)
    }
  }

  fun mapOrNull(message: String?): Failable? {
    if (message.isNullOrBlank()) return null
    return when {
      message.contains("[ERROR 400] \"page\" is out of valid range.", true) -> NoMoreItemsFoundFailure()
      message.contains("[ERROR 400] Invalid or missing API key") -> MissedPixaBayKeyFailure()
      else -> null
    }
  }

  fun mapOrDefault(message: String?): Failable {
    return mapOrNull(message) ?: UnhandledFailure()
  }
}