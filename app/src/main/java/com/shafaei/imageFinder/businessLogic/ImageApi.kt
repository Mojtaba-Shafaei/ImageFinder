package com.shafaei.imageFinder.businessLogic

import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListResponse

interface ImageApi {
  suspend fun search(query: String, page: Int): NetworkImageListResponse
}