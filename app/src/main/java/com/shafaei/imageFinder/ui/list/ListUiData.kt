package com.shafaei.imageFinder.ui.list

import com.shafaei.imageFinder.bussinessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.ui.models.ListUiParams

data class ListUiData(val param: ListUiParams, val result: List<ImageListItem>)
