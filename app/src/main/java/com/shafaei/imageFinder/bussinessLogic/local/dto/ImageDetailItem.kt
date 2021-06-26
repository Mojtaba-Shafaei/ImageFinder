package com.shafaei.imageFinder.bussinessLogic.local.dto

import com.shafaei.imageFinder.bussinessLogic.network.dto.NetworkImageDetailItem

data class ImageDetailItem(
   val id: String,
   val imageUrl: String,
   val userName: String,
   val tagList: List<String>,
   val likes: Int,
   val favorites: Int,
   val comments: Int,
) {
  companion object {
    fun from(nItem: NetworkImageDetailItem): ImageDetailItem {
      return ImageDetailItem(id = nItem.id,
         imageUrl = nItem.imageUrl,
         userName = nItem.userName,
         tagList = nItem.tagList.split(','),
         likes = nItem.likes,
         favorites = nItem.favorites,
         comments = nItem.comments)
    }
  }
}
