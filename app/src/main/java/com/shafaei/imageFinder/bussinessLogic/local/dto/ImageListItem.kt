package com.shafaei.imageFinder.bussinessLogic.local.dto

import com.shafaei.imageFinder.bussinessLogic.network.dto.NetworkImageListItem

data class ImageListItem(
   val id: String,
   val imagePreviewUrl: String,
   val userName: String,
   val tagList: String,
) {
  companion object {
    fun from(nItem: NetworkImageListItem): ImageListItem {
      return ImageListItem(
         id = nItem.id,
         imagePreviewUrl = nItem.imagePreviewUrl,
         userName = nItem.userName,
         tagList = nItem.tagList,
      )
    }
  }
}
