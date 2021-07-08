package com.shafaei.imageFinder.ui.list

import com.google.common.truth.Truth.assertThat
import com.shafaei.imageFinder.businessLogic.network.FakeNetworkImageApi
import com.shafaei.imageFinder.exceptions.NoInternetException
import com.shafaei.imageFinder.rx.ImmediateSchedulersRule
import com.shafaei.imageFinder.utils.Constants
import com.shafaei.imageFinder.utils.Lce
import io.reactivex.observers.TestObserver
import org.junit.*


class ListViewModelTest {
  private lateinit var mViewModel: ListViewModel

  @get:Rule
  var schedulers: ImmediateSchedulersRule = ImmediateSchedulersRule()

  @Before
  fun setup() {
    mViewModel = ListViewModel(FakeNetworkImageApi())
  }

  @Test
  fun `test search fruits as default, returns loadingAndData`() {

    val observer: TestObserver<Lce<ListUiData>> = TestObserver()
    mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()
    observer.awaitCount(5)

    observer.assertValueCount(2)
    observer.assertValueAt(0) { it.isLoading }
    observer.assertValueAt(1) { it.data != null }
    observer.assertNotTerminated()

    observer.dispose()
  }

  @Test
  fun `test search fruits as default, returns loadingAndFruitsData`() {

    val observer: TestObserver<Lce<ListUiData>> = TestObserver()
    mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()
    observer.awaitCount(2)
    observer.assertValueAt(0) { it.isLoading }
    observer.assertValueAt(1) { it.data != null }
    observer.assertValueAt(1) {
      it.data!!.param.page == 1 &&
         it.data!!.param.searchText == "fruits" &&
         it.data!!.result.size == Constants.PAGE_SIZE
    }
    observer.assertNotTerminated()
    observer.dispose()
  }

  @Test
  fun `test loadNextPage, returns noDuplicatedValues`() {
    val observer: TestObserver<Lce<ListUiData>> = TestObserver()
    mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()
    observer.awaitCount(2)

    mViewModel.loadNextPage()
    observer.awaitCount(4)

    observer.assertValueAt(2) { it.isLoading }
    observer.assertValueAt(3) {
      it.data!!.param.page == 2 &&
         it.data!!.param.searchText == "fruits" &&
         it.data!!.result.size == (2 * Constants.PAGE_SIZE)
    }

    assertThat(observer.values()[3].data!!.result).containsNoDuplicates()

    observer.assertNotTerminated()
    observer.dispose()
  }

  @Test
  fun `test internet is off, returns loadingAndException`() {
    mViewModel = ListViewModel(FakeNetworkImageApi(true))

    val observer: TestObserver<Lce<ListUiData>> = TestObserver()
    mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()
    observer.awaitCount(2)
    observer.assertValueAt(0) { it.isLoading }
    observer.assertValueAt(1) { it.hasError() && it.error!! is NoInternetException }
    observer.assertNotTerminated()
    observer.dispose()
  }

  @Test
  fun `test search fruits twice, returns loadingAndFruitsDataOnce`() {
    val observer: TestObserver<Lce<ListUiData>> = TestObserver()

    mViewModel.states.subscribe(observer)
    observer.awaitCount(2)

    mViewModel.search("fruits")
    observer.awaitCount(5)

    observer.assertValueCount(2)

    observer.assertNotTerminated()
    observer.dispose()
  }

  @Test
  fun `test nextPage when internet is off, returns NoInternetException`() {
    val observer: TestObserver<Lce<ListUiData>> = TestObserver()
    mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()

    observer.awaitCount(2)
    observer.assertValueAt(0) { it.isLoading }
    observer.assertValueAt(1) { it.data != null }

    mViewModel.imageApi = FakeNetworkImageApi(true)
    mViewModel.loadNextPage()

    observer.awaitCount(4)
    observer.assertValueAt(2) { it.isLoading }
    observer.assertValueAt(3) { it.hasError() && it.error is NoInternetException }

    observer.assertNotTerminated()
    observer.dispose()
  }

  @Test
  fun `test RetryLastAction when internet is off, returns NoInternetException`() {
    mViewModel = ListViewModel(FakeNetworkImageApi(true))

    val observer: TestObserver<Lce<ListUiData>> = TestObserver()
    mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()

    observer.awaitCount(2)
    observer.assertValueAt(0) { it.isLoading }
    observer.assertValueAt(1) { it.hasError() }

    mViewModel.retry()
    observer.awaitCount(4)

    observer.assertValueAt(2) { it.isLoading }
    observer.assertValueAt(3) { it.hasError() }

    observer.assertNotTerminated()
    observer.dispose()
  }

  @Test
  fun `test RetryLastAction twice when internet is off, returns internet error twice`() {
    mViewModel = ListViewModel(FakeNetworkImageApi(true))

    val observer: TestObserver<Lce<ListUiData>> = TestObserver()
    mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()

    observer.awaitCount(2)

    mViewModel.retry()
    observer.awaitCount(4)

    mViewModel.retry()
    observer.awaitCount(6)

    observer.assertValueAt(4) { it.isLoading }
    observer.assertValueAt(5) { it.hasError() }

    observer.assertNotTerminated()
    observer.dispose()
  }

  @Test
  fun `test resubscribe to ViewModel states twice(rotate screen), returns `() {

    mViewModel.states
       .test()
       .assertSubscribed()
       .awaitCount(2)
       .dispose()
    // screen is rotated and subscribe to viewModel again
    mViewModel.states
       .test()
       .awaitCount(2)
       .assertSubscribed()
       .assertValueCount(1)
       .assertValueAt(0) { it.data != null }
       .assertNotTerminated()
       .dispose()
  }
}