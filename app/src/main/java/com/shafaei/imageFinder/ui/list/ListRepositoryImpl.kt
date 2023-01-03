package com.shafaei.imageFinder.ui.list

import com.shafaei.imageFinder.businessLogic.ImageApi
import com.shafaei.imageFinder.businessLogic.helper.NetworkHelper
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.exceptions.NoMoreItemsFoundFailure
import com.shafaei.imageFinder.ui.models.ListUiParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.shafaei.imageFinder.utils.Result as Result1

class ListRepositoryImpl(private val imageApi: ImageApi) : ListRepository {
  private val _items: MutableList<ImageListItem> = ArrayList()
  private var _page: Int = 1
  private var _searchedTest: String = ""

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //// FUNCTIONS ///////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////
  override suspend fun searchImageFirstPageBy(query: String): com.shafaei.imageFinder.utils.Result<ListUiData> {
    clearCache()
    _searchedTest = query
    val result = searchImages(query, _page)
    return if (result.isSuccess) {
      val data = result.getOrNull()
      if (!data.isNullOrEmpty()) {
        _items.addAll(data)
      }
      Result1.Success(ListUiData(param = ListUiParams(page = _page, searchText = _searchedTest), result = _items))
    } else {
      Result1.Failure(result.failable)
    }
  }

  override suspend fun searchImagesNextPageBy(): Result1<ListUiData> {
    val result = searchImages(_searchedTest, _page + 1)

    return withContext(Dispatchers.Default) {
      if (result.isSuccess) {
        val newItems = result.getOrNull()
        if (!newItems.isNullOrEmpty()) {
          _items.addAll(newItems)
          _page++
          Result1.Success(ListUiData(param = ListUiParams(page = _page, searchText = _searchedTest), result = _items))
        } else {
          Result1.Failure(NoMoreItemsFoundFailure())// this failure also is handled in the business layer
        }
      } else {
        Result1.Failure(result.failable)
      }
    }
  }

  private suspend fun searchImages(query: String, page: Int): Result1<List<ImageListItem>> {
    val result = NetworkHelper.safeApiCall {
      imageApi.search(query = query, page = page)
    }

    return withContext(Dispatchers.Default) {
      if (result.isSuccess) {
        val newItems: List<ImageListItem> = result.getOrNull()!!.hits.map { ImageListItem.from(it) }
        Result1.Success(newItems)
      } else {
        Result1.Failure(result.failable)
      }

    }
  }

  override fun clearCache() {
    _items.clear()
    _page = 1
  }
}

/////////////////////////////////////
interface ListRepository {
  fun clearCache()
  suspend fun searchImageFirstPageBy(query: String): Result1<ListUiData>
  suspend fun searchImagesNextPageBy(): Result1<ListUiData>
}