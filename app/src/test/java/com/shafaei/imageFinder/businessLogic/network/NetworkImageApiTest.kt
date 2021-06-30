package com.shafaei.imageFinder.businessLogic.network

import com.shafaei.imageFinder.businessLogic.network.service.ImageService
import org.junit.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class NetworkImageApiTest {
  @get:Rule
  var rule: MockitoRule = MockitoJUnit.rule()

  @Mock
  lateinit var imageService: ImageService

  @InjectMocks
  lateinit var imageApi: NetworkImageApi

  @Before
  fun setUp() {
  }

  @After
  fun tearDown() {
  }
}