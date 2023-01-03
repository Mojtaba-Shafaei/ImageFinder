package com.shafaei.imageFinder.businessLogic.helper

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
   @SerializedName("msg")
   val msg: String? = null,
)