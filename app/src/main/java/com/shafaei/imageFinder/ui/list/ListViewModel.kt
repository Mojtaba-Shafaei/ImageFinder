package com.shafaei.imageFinder.ui.list

import androidx.lifecycle.ViewModel
import com.shafaei.imageFinder.businessLogic.ImageApi
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.exceptions.ExceptionMapper
import com.shafaei.imageFinder.kotlinExt.mapToMyException
import com.shafaei.imageFinder.ui.list.SearchAction.LoadAction
import com.shafaei.imageFinder.ui.list.SearchAction.NextPageAction
import com.shafaei.imageFinder.ui.list.SearchAction.RetryAction
import com.shafaei.imageFinder.ui.models.ListUiParams
import com.shafaei.imageFinder.utils.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class ListViewModel(var imageApi: ImageApi) : ViewModel() {
  private val mDisposables = CompositeDisposable()
  private val mSearches: PublishSubject<SearchAction> = PublishSubject.create()

  private val mStates: BehaviorSubject<Lce<ListUiData>> = BehaviorSubject.create()
  val states: Observable<Lce<ListUiData>> = mStates.distinctUntilChanged().share()

  private val items: MutableList<ImageListItem> = ArrayList()

  //////////////////////////////////////////////////////////////////////////////////////////////////
  init {
    mDisposables +=
       mSearches
          .observeOn(Schedulers.io())
          .mergeWith(RxUtils.singleDelay(value = LoadAction(query = "fruits", page = 1)))
          .distinctUntilChanged()
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
            imageApi.search(query = loadAction.query, page = loadAction.page)
               .map { result ->
                 if (result is Result.Success) {
                   val newItems: List<ImageListItem> = result.data.map { ImageListItem.from(it) }
                   items.addAll(newItems)
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
              mStates.onNext(Lce.error(it.mapToMyException()))
            }.map { false }
          }
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.computation())
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