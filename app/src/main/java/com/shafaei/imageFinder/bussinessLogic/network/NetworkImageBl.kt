package com.shafaei.imageFinder.bussinessLogic.network

import com.shafaei.imageFinder.bussinessLogic.network.dto.NetworkImageListItem
import com.shafaei.imageFinder.bussinessLogic.network.service.ImageService
import com.shafaei.imageFinder.exceptions.ExceptionMapper
import com.shafaei.imageFinder.utils.Result
import com.shafaei.imageFinder.utils.Result.Success
import io.reactivex.Single

class NetworkImageBl(private val imageService: ImageService) {
  fun search(query: String, page: Int): Single<Result<List<NetworkImageListItem>>> {
    return Single.defer {
      imageService.searchImages(
         query = query,
         page = page.toString(),
      )
         .map { retrofitResult ->
           if (RetrofitUtil.hasError(retrofitResult)) {
             val exception = RetrofitUtil.getErrors(retrofitResult)
             Result.Failure<List<NetworkImageListItem>>(exception)
           } else {
             Success(retrofitResult.response()!!.body()!!.hits)
           }
         }
         .onErrorReturn { t: Throwable -> Result.Failure(ExceptionMapper.map(t)) }
    }
  }
}