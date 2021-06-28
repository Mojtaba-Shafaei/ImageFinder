package com.shafaei.imageFinder.businessLogic.network.dto

import com.google.gson.annotations.SerializedName

data class NetworkImageListResponse(
   @SerializedName("total") val total:Int,
   @SerializedName("totalHits") val totalHits:Int,
   @SerializedName("hits") val hits:List<NetworkImageListItem>,
)
