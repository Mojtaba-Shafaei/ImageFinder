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

      else -> UnhandledException(extraMessage = t)
    }
  }

}