package com.shafaei.imageFinder.bussinessLogic.network

import com.shafaei.imageFinder.exceptions.ExceptionMapper
import com.shafaei.imageFinder.exceptions.MyException

object RetrofitUtil {
  fun <T> hasError(result: retrofit2.adapter.rxjava2.Result<T>): Boolean {
    return (result.isError or (result.response()?.isSuccessful == false))
  }

  fun <T> getErrors(result: retrofit2.adapter.rxjava2.Result<T>): MyException {
    return when {
      result.isError -> ExceptionMapper.map(result.error()!!)
      else -> {
        val errorString = result.response()!!.errorBody()!!.string()
        ExceptionMapper.map(errorString)
      }
    }
  }
}