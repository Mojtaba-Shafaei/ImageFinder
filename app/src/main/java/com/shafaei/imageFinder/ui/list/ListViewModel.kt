package com.shafaei.imageFinder.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.businessLogic.network.NetworkImageBl
import com.shafaei.imageFinder.businessLogic.network.RetrofitHelper
import com.shafaei.imageFinder.businessLogic.network.service.ImageService
import com.shafaei.imageFinder.exceptions.ExceptionMapper
import com.shafaei.imageFinder.kotlinExt.mapToMyException
import com.shafaei.imageFinder.ui.list.SearchAction.LoadAction
import com.shafaei.imageFinder.ui.list.SearchAction.NextPageAction
import com.shafaei.imageFinder.ui.list.SearchAction.RetryAction
import com.shafaei.imageFinder.ui.models.ListUiParams
import com.shafaei.imageFinder.utils.Lce
import com.shafaei.imageFinder.utils.Result
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class ListViewModel : ViewModel() {
  private val mDisposables = CompositeDisposable()
  private val mSearches: PublishSubject<SearchAction> = PublishSubject.create()

  private val mStates: BehaviorSubject<Lce<ListUiData>> = BehaviorSubject.create()
  val states: Observable<Lce<ListUiData>> = mStates.distinctUntilChanged()

  private val imageBl: NetworkImageBl by lazy { NetworkImageBl(imageService = RetrofitHelper.retrofit.create(ImageService::class.java)) }

  private val items: MutableList<ImageListItem> = ArrayList()

  //////////////////////////////////////////////////////////////////////////////////////////////////
  init {
    mDisposables +=
       mSearches
          .mergeWith(Single.just(LoadAction(query = "fruits", page = 1))) //default action to search when application start
          .scan { old, new ->
            when (new) {
              is RetryAction -> old as LoadAction
              is NextPageAction -> {
                val action = (old as LoadAction)
                action.copy(page = action.page + 1)
              }
              is LoadAction -> new
              else -> throw IllegalArgumentException("action $new not handled")
            }
          }
          .map { action -> action as LoadAction }
          .doOnNext {
            if (it.page == 1) {
              items.clear()
            }
          }
          .flatMap { loadAction ->
            imageBl.search(query = loadAction.query, page = loadAction.page)
               .map { result ->
                 if (result is Result.Success) {
                   items.addAll(result.data.map { ImageListItem.from(it) })
                   Lce.data(ListUiData(param = ListUiParams(page = loadAction.page, searchText = loadAction.query), result = items))
                 } else {
                   Lce.error((result as Result.Failure).exception)
                 }
               }
               .onErrorReturn { throwable: Throwable -> Lce.error(throwable.mapToMyException()) }
               .toObservable()
               .startWith(Lce.loading())
          }
          .retryWhen { errors ->
            errors.doOnNext {
              Log.e("TAG", "Main Stream got exception", it)
              mStates.onNext(Lce.error(it.mapToMyException()))
            }.map { false }
          }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
             {
               mStates.onNext(it)
             }, { mStates.onNext(Lce.error(ExceptionMapper.map(it))) })
  }

  override fun onCleared() {
    super.onCleared()
    mDisposables.clear()
  }

  fun retry() {
    mSearches.onNext(RetryAction())
  }

  fun search(query: String) {
    mSearches.onNext(LoadAction(page = 1, query = query))
  }

  fun loadNextPage() {
    mSearches.onNext(NextPageAction())
  }
}