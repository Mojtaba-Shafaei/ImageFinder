package com.shafaei.imageFinder.exceptions

import android.util.Log
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExceptionMapper {
  fun map(t: Throwable): MyException {
    Log.e("ExceptionMapper", "map", t)
    return when {
      t is UnknownHostException || t is SocketTimeoutException -> NoInternetException()

      else -> UnhandledException()
    }
  }

  fun map(t: String): MyException {
    Log.e("ExceptionMapper", t)
    return when {
      t == "[ERROR 400] \"page\" is out of valid range." -> CanNotGetMoreResultException()

      else -> UnhandledException()
    }
  }

}