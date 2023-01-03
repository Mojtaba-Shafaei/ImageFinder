package com.shafaei.imageFinder.ui.list

sealed interface SearchIntents {
  data class LoadFirstPageIntent(val query: String) : SearchIntents
  object RetryIntent : SearchIntents
  object LoadNextPageIntent : SearchIntents
  object NothingIntent : SearchIntents
}