package com.shafaei.imageFinder.ui.list

import androidx.lifecycle.ViewModel
import com.shafaei.imageFinder.utils.Lce
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class ListViewModel : ViewModel() {
  private val mDisposables = CompositeDisposable()
  private val mStates: BehaviorSubject<Lce<ListUiData>> = BehaviorSubject.create()
  val states: Observable<Lce<ListUiData>> = mStates

  //////////////////////////////////////////////////////////////////////////////////////////////////
  init {

  }

  override fun onCleared() {
    super.onCleared()
    mDisposables.clear()
  }
}