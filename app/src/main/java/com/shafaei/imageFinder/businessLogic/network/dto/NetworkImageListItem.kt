package com.shafaei.imageFinder.businessLogic.network.dto

import com.google.gson.annotations.SerializedName

data class NetworkImageListItem(
   @SerializedName("id") val id: String,
   @SerializedName("previewURL") val imagePreviewUrl: String,
   @SerializedName("user") val userName: String,
   @SerializedName("tags") val tagList: String,
   @SerializedName("largeImageURL") val imageUrl: String,
   @SerializedName("likes") val likes: Int?,
   @SerializedName("favorites") val favorites: Int?,
   @SerializedName("comments") val comments: Int?,
)
