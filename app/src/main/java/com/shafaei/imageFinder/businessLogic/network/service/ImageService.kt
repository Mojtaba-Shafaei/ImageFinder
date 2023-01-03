package com.shafaei.imageFinder.businessLogic.network.service

import com.shafaei.imageFinder.businessLogic.Constants
import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListResponse
import com.shafaei.imageFinder.utils.Constants.PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

  @GET("api")
  suspend fun searchImages(
     @Query("key") key: String = Constants.API_KEY,
     @Query("q") query: String,
     @Query("page") page: String?,
     @Query("per_page") perPage: Int = PAGE_SIZE,
     @Query("order") order: String = "latest",
  ): NetworkImageListResponse
}