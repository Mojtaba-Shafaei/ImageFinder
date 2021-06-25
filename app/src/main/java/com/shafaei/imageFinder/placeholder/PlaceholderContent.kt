package com.shafaei.imageFinder.placeholder

import com.shafaei.imageFinder.bussinessLogic.local.ImageDetailItem
import com.shafaei.imageFinder.bussinessLogic.local.ImageListItem
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

  /**
   * A map of sample (placeholder) items, by ID.
   */
  val ITEM_MAP: MutableMap<String, ImageListItem> = HashMap()

  private val COUNT = 25

  val ITEM: ImageDetailItem = ImageDetailItem(id = "2295434", imageUrl = "https://pixabay.com/get/g4ef24a92f252ca3e37696534877219b1c49a571f0bcf6b930dfa288108aa4fff723369d0418c201817df7c2976e5b1cde665d0e9b9fe1a844bb9774d650bbc7e_1280.jpg",
     userName = "JillWellington",
     tagList = listOf("spring bird", "bird", "tit"),
     likes = 1508, favorites = 1542, comments = 164)

  init {
    // Add some sample items.
    for (i in 1..COUNT) {
      addItem(createPlaceholderItem(i))
    }
  }

  private fun addItem(item: ImageListItem) {
    ITEMS.add(item)
    ITEM_MAP.put(item.id, item)
  }

  private fun createPlaceholderItem(position: Int): ImageListItem {
    return ImageListItem(
       id = "2295434",
       imagePreviewUrl = "https://cdn.pixabay.com/photo/2018/01/28/11/24/sunflower-3113318_150.jpg",
       userName = "Mojtaba Shafaei :)",
       tagList = "sunflower, nature, flora",
    )
  }
}