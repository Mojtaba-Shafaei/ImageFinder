package com.shafaei.imageFinder.businessLogic

import com.shafaei.imageFinder.businessLogic.network.dto.NetworkImageListItem
import com.shafaei.imageFinder.utils.Result
import io.reactivex.Single

interface ImageApi {
  fun search(query: String, page: Int): Single<Result<List<NetworkImageListItem>>>
}