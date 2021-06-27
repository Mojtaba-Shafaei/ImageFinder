package com.shafaei.imageFinder.ui.list

sealed interface SearchAction {
  data class LoadAction(val page: Int, val query: String) : SearchAction
  class RetryAction : SearchAction
  class NextPageAction : SearchAction
}