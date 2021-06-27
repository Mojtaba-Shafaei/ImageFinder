package com.shafaei.imageFinder.kotlinExt

import com.shafaei.imageFinder.exceptions.ExceptionMapper
import com.shafaei.imageFinder.exceptions.MyException

fun Throwable.mapToMyException(): MyException {
  return ExceptionMapper.map(this)
}