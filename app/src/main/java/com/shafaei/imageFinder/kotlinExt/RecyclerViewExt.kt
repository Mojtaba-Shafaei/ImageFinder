package com.shafaei.imageFinder.kotlinExt

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


private fun RecyclerView.setVerticalLayoutManager() {
  this.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
}

fun RecyclerView.isReachedToTheLastItem(totalItems: Int, threshold: Int = 5): Boolean {
  val lastVisibleItemPosition = (this.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
  return if (lastVisibleItemPosition >= totalItems - threshold) {
    Log.d("TAG", "LAST ITEM REACHED, totalItems = $totalItems, threshold = $threshold")
    true
  } else
    false
}

fun RecyclerView.init() {
  this.setVerticalLayoutManager()
  this.isVerticalScrollBarEnabled = true
  this.verticalScrollbarPosition = RecyclerView.SCROLLBAR_POSITION_LEFT
}
