package com.shafaei.imageFinder.businessLogic.network

import com.shafaei.imageFinder.businessLogic.ImageApi
import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListResponse
import com.shafaei.imageFinder.businessLogic.network.service.ImageService

class NetworkImageApi(private val imageService: ImageService) : ImageApi {
  override suspend fun search(query: String, page: Int): NetworkImageListResponse {
    return imageService.searchImages(
       query = query,
       page = page.toString(),
    )
  }
}