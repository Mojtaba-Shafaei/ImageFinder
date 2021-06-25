package com.shafaei.imageFinder.placeholder

import com.google.gson.annotations.SerializedName

data class NetworkPixaBayItem(
   @SerializedName("id") val id: String,
   @SerializedName("largeImageURL") val imageUrl: String,
   @SerializedName("previewURL") val imagePreviewUrl: String,
   @SerializedName("user") val userName: String,
   @SerializedName("tags") val tagList: String,
   @SerializedName("likes") val likes: Int,
   @SerializedName("favorites") val favorites: Int,
   @SerializedName("comments") val comments: Int,
)
