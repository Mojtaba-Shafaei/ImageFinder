package com.shafaei.imageFinder.bussinessLogic.network.service

import com.shafaei.imageFinder.bussinessLogic.network.dto.NetworkImageListResponse
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

  @GET("/")
  fun searchImages(
     @Query("key ") key: String = "22232344-2a6d17f210567442d2c1d8ce4",
     @Query("q") query: String,
     @Query("page") page: String?,
     @Query("per_page") perPage: Int = 20,
  ): Single<Result<NetworkImageListResponse>>
}