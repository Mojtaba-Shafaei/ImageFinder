package com.shafaei.imageFinder.placeholder

import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

  /**
   * An array of sample (placeholder) items.
   */
  val ITEMS: MutableList<PixaBayItem> = ArrayList()

  /**
   * A map of sample (placeholder) items, by ID.
   */
  val ITEM_MAP: MutableMap<String, PixaBayItem> = HashMap()

  private val COUNT = 25

  init {
    // Add some sample items.
    for (i in 1..COUNT) {
      addItem(createPlaceholderItem(i))
    }
  }

  private fun addItem(item: PixaBayItem) {
    ITEMS.add(item)
    ITEM_MAP.put(item.id, item)
  }

  private fun createPlaceholderItem(position: Int): PixaBayItem {
    return PixaBayItem(id = position.toString(), imageUrl = "https://pixabay.com/get/ga619bfedff2a069d72969cb64ec375111e442a219bd6f145e049d91d6a745cc0e0a3d05c686d4db195ed43486cf4a903fd959a632a9806f80d6adbeb9d48ef72_1280.jpg", imagePreviewUrl = "", userName = "bichnguyenvo", tagList = listOf<String>("sunflower", "nature", "flora"))
  }
}