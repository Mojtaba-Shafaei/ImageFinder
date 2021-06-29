package com.shafaei.imageFinder.businessLogic.network

import com.shafaei.imageFinder.businessLogic.network.service.ImageService
import com.shafaei.imageFinder.utils.Result.Success
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue

internal class NetworkImageBlTest {
  private lateinit var imageBl: NetworkImageBl
  private lateinit var scheduler: Scheduler

  @BeforeEach
  fun setUp() {
    scheduler = Schedulers.single()
  //todo  imageBl = NetworkImageBl(RetrofitHelper.retrofit.create(ImageService::class.java))
  }

  @AfterEach
  fun tearDown() {

  }

  @Test
  fun search_YellowFlower_Page1() {
    val result = imageBl.search(query = "Yellow Flower", page = 1)
       .subscribeOn(scheduler)
       .blockingGet()

    assertTrue(result is Success) { "Result is Not Successful" }
    assertTrue((result as Success).data.isNotEmpty())
  }

  @Test
  fun search_YellowFlower_Page2() {
    val result = imageBl.search(query = "Yellow Flower", page = 2)
       .subscribeOn(scheduler)
       .blockingGet()

    assertTrue(result is Success) { "Result is Not Successful" }
    assertTrue((result as Success).data.isNotEmpty())
  }

  @Test
  fun `search_YellowFlower_Page1,2,3AreDifferent`() {
    val query = "Yellow Flower"
    val result1 = imageBl.search(query = query, page = 1)
       .subscribeOn(scheduler)
       .blockingGet()

    val result2 = imageBl.search(query = query, page = 2)
       .subscribeOn(scheduler)
       .blockingGet()

    val result3 = imageBl.search(query = query, page = 3)
       .subscribeOn(scheduler)
       .blockingGet()

    assertTrue(result1 is Success) { "Result page1 is Not Successful" }
    assertTrue(result2 is Success) { "Result page2 is Not Successful" }
    assertTrue(result3 is Success) { "Result page3 is Not Successful" }

    val data1 = (result1 as Success).data
    val data2 = (result2 as Success).data
    val data3 = (result3 as Success).data

    assertNotEquals(data1, data2)
    assertNotEquals(data1, data3)
    assertNotEquals(data2, data3)

    assertTrue(data1.intersect(data2).isNullOrEmpty()) { "There Are The Same Item(s) in Both Pages1,2" }
    assertTrue(data2.intersect(data3).isNullOrEmpty()) { "There Are The Same Item(s) in Both Pages2,3" }
    assertTrue(data1.intersect(data3).isNullOrEmpty()) { "There Are The Same Item(s) in Both Pages1,3" }
  }
}