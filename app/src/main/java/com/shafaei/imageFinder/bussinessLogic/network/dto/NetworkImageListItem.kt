package com.shafaei.imageFinder.bussinessLogic.network.dto

import com.google.gson.annotations.SerializedName

data class NetworkImageListItem(
   @SerializedName("id") val id: String,
   @SerializedName("previewURL") val imagePreviewUrl: String,
   @SerializedName("user") val userName: String,
   @SerializedName("tags") val tagList: String,
)
