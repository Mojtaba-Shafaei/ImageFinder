@file:OptIn(ExperimentalCoroutinesApi::class)

package com.shafaei.imageFinder.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.shafaei.AssetFilesReader
import com.shafaei.imageFinder.businessLogic.local.dto.ImageListItem
import com.shafaei.imageFinder.exceptions.NoInternetFailure
import com.shafaei.imageFinder.ui.models.ListUiParams
import com.shafaei.imageFinder.utils.Constants
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.*
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.*
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import com.shafaei.imageFinder.utils.Result as Result1

@ExtendWith(MockKExtension::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class ListViewModelTest : KoinTest {
  private val dispatcher = StandardTestDispatcher()
  private val _defaultSearch = "fruits"

  @get:Rule
  val rule = InstantTaskExecutorRule()

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    Dispatchers.setMain(dispatcher)
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
    unmockkAll()
    dispatcher.cancel()
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
  @DisplayName("Init ViewModel")
  @Order(10)
  inner class InitViewModel {
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    @DisplayName("Load default search text result")
    @Order(1)
    inner class LoadFirstPage {
      /**
       *  This states should be populated automatically, when the viewModel is created.
       *  In fact, the view-model will search the [_defaultSearch] text, and will return
       *  the first page of data.
       *
       *  ## desired ui-states:
       *  1. loading
       *  2. data - the first page data that is loaded from internet just now
       */
      @Order(5)
      @Test
      fun initViewModel_returnsLoadingAndFruitsDataPage1_ifSearchFruitsAsDefault() = runTest(dispatchTimeoutMs = 1000) {
        val mRepo: ListRepository = mockk()
        every { mRepo.clearCache() }
        val imageListItems = AssetFilesReader.readFruitsPage1().map { ImageListItem.from(it) }
        coEvery { mRepo.searchImageFirstPageBy(any()) }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 1, searchText = _defaultSearch), result = imageListItems)))
        val mViewModel = ListViewModel(mRepo = mRepo)

        mViewModel.states
           .test {
             awaitItem().should {
               it.isLoading.shouldBeTrue()
             }

             awaitItem().should {
               it.data.shouldNotBeNull()
               it.data.shouldBeInstanceOf<ListUiData>()
                  .should { listUiData ->
                    listUiData.param.page.shouldBeEqualComparingTo(1)
                    listUiData.param.searchText.shouldBeEqualIgnoringCase(_defaultSearch)
                    listUiData.result.size.shouldBeEqualComparingTo(Constants.PAGE_SIZE)
                  }
             }
             expectNoEvents()
           }
      }
    }
  }

  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
  @DisplayName("Load First Page|Search By User")
  @Order(15)
  inner class LoadFirstPage {
    /**
     *  This activity is started and user searches a valid text that have to have some data.
     *
     *  ## desired ui-states:
     *  1. loading
     *  2. data - the result of default-search text
     *  3. loading
     *  4. data - the result of searched text that is not empty
     */
    @Order(5)
    @Test
    fun loadFirstPage_returnsLoadingAndDataStates_ifSearchAValidText() = runTest(dispatchTimeoutMs = 1000) {
      val validSearchedText = "this subjects has result on the server"
      val mRepo: ListRepository = mockk()
      every { mRepo.clearCache() }
      val imageListItems = AssetFilesReader.readFruitsPage1().map { ImageListItem.from(it) }
      coEvery { mRepo.searchImageFirstPageBy(_defaultSearch) }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 1, searchText = _defaultSearch), result = imageListItems)))
      coEvery { mRepo.searchImageFirstPageBy(validSearchedText) }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 1, searchText = validSearchedText), result = imageListItems)))
      val mViewModel = ListViewModel(mRepo = mRepo)

      mViewModel.search(validSearchedText)
      mViewModel.states
         .test {
           repeat(2) {
             awaitItem()
           }

           awaitItem().should {
             it.isLoading.shouldBeTrue()
           }
           awaitItem().should {
             it.data.shouldNotBeNull()
                .result.shouldNotBeEmpty()
           }
           expectNoEvents()
         }
    }

    /**
     * ## desired ui-states:
     * 1. loading
     * 2. data - an empty list, (the searched text has no any result)
     *
     */
    @Order(10)
    @Test
    fun loadFirstPage_returnsLoadingAndEmptyDataStates_ifSearchNotValidText() = runTest(dispatchTimeoutMs = 1000) {
      val noValidSearchedText = "this subjects has no result on the server"
      val mRepo: ListRepository = mockk()
      every { mRepo.clearCache() }
      val imageListItems = AssetFilesReader.readFruitsPage1().map { ImageListItem.from(it) }
      coEvery { mRepo.searchImageFirstPageBy(_defaultSearch) }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 1, searchText = _defaultSearch), result = imageListItems)))
      coEvery { mRepo.searchImageFirstPageBy(noValidSearchedText) }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 1, searchText = noValidSearchedText), result = emptyList())))
      val mViewModel = ListViewModel(mRepo = mRepo)

      mViewModel.search(noValidSearchedText)
      mViewModel.states
         .test {
           repeat(2) {
             awaitItem()
           }

           awaitItem().should {
             it.isLoading.shouldBeTrue()
           }
           awaitItem().should {
             it.data.shouldNotBeNull()
                .result.shouldBeEmpty()
           }
           expectNoEvents()
         }
    }
  }


  @Nested
  @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
  @DisplayName("Load Next Page")
  @Order(20)
  inner class LoadNextPage {
    /**
     * ## desired ui-states:
     * 1. loading
     * 2. data - the first page data that is loaded from internet just now.
     * 3. loading
     * 4. data - combination of the first page and the second page data, without any duplicate
     *
     */
    @Order(5)
    @Test
    fun loadNextPage_returnsNoDuplicatedData_ifRepositoryWorksGood() = runTest(dispatchTimeoutMs = 1000) {
      val mRepo: ListRepository = mockk()
      val imageListItems = AssetFilesReader.readFruitsPage1().map { ImageListItem.from(it) }
      val imageListItems2 = AssetFilesReader.readFruitsPage2().map { ImageListItem.from(it) }
      every { mRepo.clearCache() }
      coEvery { mRepo.searchImageFirstPageBy(_defaultSearch) }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 1, searchText = _defaultSearch), result = imageListItems)))
      coEvery { mRepo.searchImagesNextPageBy() }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 2, searchText = _defaultSearch), result = imageListItems + imageListItems2)))
      val mViewModel = ListViewModel(mRepo = mRepo)

      mViewModel.loadNextPage()
      mViewModel.states
         .test {
           repeat(2) { awaitItem() }

           awaitItem().should {
             it.isLoading.shouldBeTrue()
           }
           awaitItem().should {
             it.data.shouldNotBeNull()
             it.data.shouldBeInstanceOf<ListUiData>()
                .should { listUiData ->
                  listUiData.param.page.shouldBeEqualComparingTo(2)
                  listUiData.param.searchText.shouldBeEqualIgnoringCase(_defaultSearch)

                  listUiData.result.size.shouldBeEqualComparingTo(2 * Constants.PAGE_SIZE)
                  listUiData.result.shouldNotContainDuplicates()
                }
           }
           expectNoEvents()
         }
    }

    /**
     * ## desired ui-states:
     * 1. loading
     * 2. data - the first page data that is loaded from internet just now.
     * 3. loading
     * 4. internet error + previous loaded data.
     *
     */
    @Order(10)
    @Test
    fun loadNextPage_returnsNoInternetFailure_ifInternetIsOff() = runTest(dispatchTimeoutMs = 1000) {
      val mRepo: ListRepository = mockk()
      val imageListItems = AssetFilesReader.readFruitsPage1().map { ImageListItem.from(it) }
      every { mRepo.clearCache() }
      coEvery { mRepo.searchImageFirstPageBy(_defaultSearch) }.returns(Result1.Success(ListUiData(param = ListUiParams(page = 1, searchText = _defaultSearch), result = imageListItems)))
      coEvery { mRepo.searchImagesNextPageBy() }.returns(Result1.Failure(NoInternetFailure()))
      val mViewModel = ListViewModel(mRepo = mRepo)

      mViewModel.loadNextPage()
      mViewModel.states
         .test {
           repeat(2) { awaitItem() }
           awaitItem().should {
             it.isLoading.shouldBeTrue()
           }
           awaitItem().should {
             it.data.shouldNotBeNull()
                .should { listUiData ->
                  listUiData.param.page.shouldBeEqualComparingTo(1)
                  listUiData.param.searchText.shouldBeEqualIgnoringCase(_defaultSearch)

                  listUiData.result.size.shouldBeEqualComparingTo(Constants.PAGE_SIZE)
                }
             it.error.shouldBeInstanceOf<NoInternetFailure>()
           }
           expectNoEvents()
         }
    }

  }

  @Test
  fun `test internet is off, returns loadingAndException`() = runTest(dispatchTimeoutMs = 1000) {
    /*mViewModel.states
       .subscribe(observer)

    observer.assertSubscribed()
    observer.awaitCount(2)
    observer.assertValueAt(0) { it.isLoading }
    observer.assertValueAt(1) { it.hasError() && it.error!! is NoInternetFailure }
    observer.assertNotTerminated()
    observer.dispose()*/
    TODO("This is not implemented yet")
  }

  @Test
  fun `test search fruits twice, returns loadingAndFruitsDataOnce`() = runTest(dispatchTimeoutMs = 1000) {
    /*val observer: TestObserver<MviState<ListUiData>> = TestObserver()

    mViewModel.states.subscribe(observer)
    observer.awaitCount(2)

    mViewModel.search("fruits")
    observer.awaitCount(5)

    observer.assertValueCount(2)

    observer.assertNotTerminated()
    observer.dispose()*/
    TODO("This is not implemented yet")
  }


  @Test
  fun `test RetryLastAction when internet is off, returns NoInternetException`() = runTest(dispatchTimeoutMs = 1000) {
//    mViewModel = ListViewModel(FakeNetworkImageApi(true))
//
//    val observer: TestObserver<MviState<ListUiData>> = TestObserver()
//    mViewModel.states
//       .subscribe(observer)
//
//    observer.assertSubscribed()
//
//    observer.awaitCount(2)
//    observer.assertValueAt(0) { it.isLoading }
//    observer.assertValueAt(1) { it.hasError() }
//
//    mViewModel.retry()
//    observer.awaitCount(4)
//
//    observer.assertValueAt(2) { it.isLoading }
//    observer.assertValueAt(3) { it.hasError() }
//
//    observer.assertNotTerminated()
//    observer.dispose()
    TODO("This is not implemented yet")
  }

  @Test
  fun `test RetryLastAction twice when internet is off, returns internet error twice`() = runTest(dispatchTimeoutMs = 1000) {
    /*mViewModel = ListViewModel(FakeNetworkImageApi(true))

    val observer: TestObserver<MviState<ListUiData>> = TestObserver()
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
    observer.dispose()*/
    TODO("This is not implemented yet")
  }

  @Test
  fun `test resubscribe to ViewModel states twice(rotate screen), returns `() = runTest(dispatchTimeoutMs = 1000) {
/*
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

 */

    TODO("This is not implemented yet")
  }
}