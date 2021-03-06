package com.shafaei.imageFinder.businessLogic.local.dto

import android.os.Parcelable
import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageListItem(
   val id: String,
   val imagePreviewUrl: String,
   val userName: String,
   val tagList: List<String>,
   val imageUrl: String,
   val likes: Int,
   val favorites: Int,
   val comments: Int,
) : Parcelable {
  companion object {
    fun from(nItem: NetworkImageListItem): ImageListItem {
      return ImageListItem(
         id = nItem.id,
         imagePreviewUrl = nItem.imagePreviewUrl,
         userName = nItem.userName,
         tagList = nItem.tagList.split(','),
         imageUrl = nItem.imageUrl,
         likes = nItem.likes ?: 0,
         favorites = nItem.favorites ?: 0,
         comments = nItem.comments ?: 0,
      )
    }
  }
}
