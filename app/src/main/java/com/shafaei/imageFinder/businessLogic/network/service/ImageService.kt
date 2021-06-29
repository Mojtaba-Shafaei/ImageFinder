package com.shafaei.imageFinder.businessLogic.network.service

import com.shafaei.imageFinder.BuildConfig
import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListResponse
import com.shafaei.imageFinder.utils.Constants.PAGE_SIZE
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

  @GET("api")
  fun searchImages(
     @Query("key") key: String = BuildConfig.API_KEY,
     @Query("q") query: String,
     @Query("page") page: String?,
     @Query("per_page") perPage: Int = PAGE_SIZE,
     @Query("order") order: String = "latest",
  ): Single<Result<NetworkImageListResponse>>
}