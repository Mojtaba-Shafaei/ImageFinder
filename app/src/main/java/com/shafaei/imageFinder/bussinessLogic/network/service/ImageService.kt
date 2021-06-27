package com.shafaei.imageFinder.bussinessLogic.network.service

import com.shafaei.imageFinder.bussinessLogic.network.dto.NetworkImageListResponse
import com.shafaei.imageFinder.utils.Constants.PAGE_SIZE
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

  @GET("api")
  fun searchImages(
     @Query("key") key: String = "22232344-2a6d17f210567442d2c1d8ce4",
     @Query("q") query: String,
     @Query("page") page: String?,
     @Query("per_page") perPage: Int = PAGE_SIZE,
  ): Single<Result<NetworkImageListResponse>>
}