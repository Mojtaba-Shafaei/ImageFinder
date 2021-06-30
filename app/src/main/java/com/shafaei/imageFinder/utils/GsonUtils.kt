package com.shafaei.imageFinder.utils

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtils {
  inline fun <reified T> typeOfList(): Type {
    return object : TypeToken<List<T>>() {}.type
  }
}