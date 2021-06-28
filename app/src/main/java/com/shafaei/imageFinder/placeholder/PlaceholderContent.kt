package com.shafaei.imageFinder.placeholder

import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object PlaceholderContent {

  /**
   * An array of sample (ImageList) items.
   */
  val ITEMS: MutableList<ImageListItem> = ArrayList()
  private val COUNT = 25

  init {
    // Add some sample items.
    for (i in 1..COUNT) {
      addItem(createPlaceholderItem(i))
    }
  }

  private fun addItem(item: ImageListItem) {
    ITEMS.add(item)
  }

  private fun createPlaceholderItem(position: Int): ImageListItem {
    return ImageListItem(
       id = "2295434",
       imagePreviewUrl = "https://cdn.pixabay.com/photo/2018/01/28/11/24/sunflower-3113318_150.jpg",
       userName = "$position Mojtaba Shafaei :)",
       tagList = listOf("sunflower", "nature", "flora"),
       imageUrl = "https://cdn.pixabay.com/photo/2018/01/28/11/24/sunflower-3113318_150.jpg",
       likes = position * 2,
       favorites = position * 3,
       comments = position * 10,
    )
  }
}