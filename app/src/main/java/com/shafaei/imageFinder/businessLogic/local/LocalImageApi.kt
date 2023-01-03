package com.shafaei.imageFinder.businessLogic.local

import com.shafaei.imageFinder.businessLogic.ImageApi
import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListResponse
import com.shafaei.imageFinder.businessLogic.network.service.ImageService

class LocalImageApi(private val imageService: ImageService) : ImageApi {
  override suspend fun search(query: String, page: Int): NetworkImageListResponse {
    return NetworkImageListResponse(total = 0, totalHits = 0, hits = emptyList())
  }
}