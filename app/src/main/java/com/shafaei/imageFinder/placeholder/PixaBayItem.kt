package com.shafaei.imageFinder.placeholder

data class PixaBayItem(
   val id: String,
   val imageUrl: String,
   val imagePreviewUrl: String,
   val userName: String,
   val tagList: List<String>,
   val likes: Int,
   val favorites: Int,
   val comments: Int,
) {
  companion object {
    fun from(networkPixaBayItem: NetworkPixaBayItem): PixaBayItem {
      return PixaBayItem(id = networkPixaBayItem.id,
         imageUrl = networkPixaBayItem.imageUrl,
         imagePreviewUrl = networkPixaBayItem.imagePreviewUrl,
         userName = networkPixaBayItem.userName,
         tagList = networkPixaBayItem.tagList.split(','),
         likes = networkPixaBayItem.likes,
         favorites = networkPixaBayItem.favorites,
         comments = networkPixaBayItem.comments)
    }
  }
}
