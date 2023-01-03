package com.shafaei.imageFinder.businessLogic.helper

import com.shafaei.imageFinder.exceptions.ExceptionMapper
import com.shafaei.imageFinder.exceptions.NoInternetFailure
import com.shafaei.imageFinder.kotlinExt.asFailableOrNull
import com.shafaei.imageFinder.kotlinExt.asFailure
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import com.shafaei.imageFinder.utils.Result as Result1

object NetworkHelper {
  suspend fun <T> safeApiCall(
     dispatcher: CoroutineDispatcher = Dispatchers.IO,
     apiCall: suspend () -> T,
  ): Result1<T> {
    return withContext(dispatcher) {
      try {
        Result1.Success(apiCall.invoke())
      } catch (throwable: Throwable) {
        when (throwable) {
          is IOException -> Result1.Failure(NoInternetFailure())
          is HttpException -> {
            val code = throwable.code()
            val errorResponse = convertErrorBodyOrNull(throwable)
            Result1.Failure(throwable.asFailableOrNull() ?: ExceptionMapper.mapOrDefault(code.toString() + errorResponse + throwable.toString()))
          }
          else -> {
            println(throwable)
            throwable.asFailure()
          }
        }
      }
    }
  }

  private fun convertErrorBodyOrNull(throwable: HttpException): String? {
    return try {
      throwable.response()?.errorBody()?.source()?.readUtf8()
    } catch (exception: Exception) {
      null
    }
  }
}