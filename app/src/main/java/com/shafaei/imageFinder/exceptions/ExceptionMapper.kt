package com.shafaei.imageFinder.exceptions

import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExceptionMapper {
  fun map(t: Throwable): MyException {
    return when {
      t is UnknownHostException || t is SocketTimeoutException -> NoInternetException()

      else -> UnhandledException(extraMessage = t.message)
    }
  }

  fun map(t: String): MyException {
    return when {
      t == "[ERROR 400] \"page\" is out of valid range." -> CanNotGetMoreResultException()
      t.startsWith("[ERROR 400] Invalid or missing API key") -> MissedPixabayKeyException()
      else -> UnhandledException(extraMessage = t)
    }
  }

}