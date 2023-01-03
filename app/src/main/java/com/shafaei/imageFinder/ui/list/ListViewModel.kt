@file:OptIn(FlowPreview::class)

package com.shafaei.imageFinder.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shafaei.imageFinder.kotlinExt.asErrorState
import com.shafaei.imageFinder.ui.list.SearchIntents.LoadFirstPageIntent
import com.shafaei.imageFinder.ui.list.SearchIntents.LoadNextPageIntent
import com.shafaei.imageFinder.ui.list.SearchIntents.NothingIntent
import com.shafaei.imageFinder.ui.list.SearchIntents.RetryIntent
import com.shafaei.imageFinder.utils.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow.SUSPEND
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ListViewModel(val mRepo: ListRepository) : ViewModel(), KoinComponent {
  private val _intents = Channel<SearchIntents>(capacity = 5, onBufferOverflow = SUSPEND)

  private val _states = Channel<MviState<ListUiData>>()
  val states: Flow<MviState<ListUiData>> = _states.receiveAsFlow().distinctUntilChanged().shareIn(viewModelScope, SharingStarted.Eagerly, 1)

  //////////////////////////////////////////////////////////////////////////////////////////////////
  init {
    viewModelScope.launch {
      launch {
        merge(flowOf(LoadFirstPageIntent(query = "fruits")), _intents.consumeAsFlow())
           .onEach { println("intent = $it") }
           .scan(NothingIntent as SearchIntents) { old, new ->
             if (new is RetryIntent) {
               old
             } else {
               new
             }
           }
           .filter { it !is NothingIntent }
           .flatMapConcat { intent ->
             flow {
               emit(MviState.loading())
               val result = if (intent is LoadFirstPageIntent) {
                 mRepo.searchImageFirstPageBy(intent.query)
               } else {
                 mRepo.searchImagesNextPageBy()
               }
               if (result.isSuccess) {
                 emit(MviState.data(result.getOrNull()!!))
               } else {
                 emit(MviState.error(result.failable))
               }
             }
           }
           .catch { throwable: Throwable -> emit(throwable.asErrorState()) }
           .retry(2)
           .scan(MviState.idle<ListUiData>()) { old, new ->
             if (new.data == null && old.data != null) {
               new.copy(data = old.data)
             } else {
               new
             }
           }
           .drop(1) // the first item is idle state, so, I dropped it
           .collect {
             _states.send(it)
           }
      }
    }
  }

  fun retry() {
    viewModelScope.launch {
      _intents.send(RetryIntent)
    }
  }

  fun search(query: String) {
    viewModelScope.launch {
      _intents.send(LoadFirstPageIntent(query = query))
    }
  }

  fun loadNextPage() {
    viewModelScope.launch {
      _intents.send(LoadNextPageIntent)
    }
  }
}