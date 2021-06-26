package com.shafaei.imageFinder.exceptions

import java.net.UnknownHostException

object ExceptionMapper {
  fun map(t: Throwable): MyException {
    return when {
      t is UnknownHostException -> NoInternetException()

      else -> UnhandledException()
    }
  }

  fun map(t: String): MyException {
    return when {
      t == "[ERROR 400] \"page\" is out of valid range." -> CanNotGetMoreResultException()

      else -> UnhandledException()
    }
  }

}