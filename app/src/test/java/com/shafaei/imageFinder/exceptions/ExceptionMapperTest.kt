package com.shafaei.imageFinder.exceptions

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ExceptionMapperTest {

  @Test
  fun `test map UnknownHostException, returns NoInternetException`() {
    val e = ExceptionMapper.map(UnknownHostException())
    assertThat(e).isInstanceOf(NoInternetException::class.java)
  }

  @Test
  fun `test map SocketTimeoutException, returns NoInternetException`() {
    val e = ExceptionMapper.map(SocketTimeoutException())
    assertThat(e).isInstanceOf(NoInternetException::class.java)
  }

  @Test
  fun `test map IllegalStateException, returns UnhandledException`() {
    val e = ExceptionMapper.map(IllegalStateException())
    assertThat(e).isInstanceOf(UnhandledException::class.java)
  }

  @Test
  fun `test API error page is out of range, returns CanNotGetMoreResultException`() {
    val e = ExceptionMapper.map("[ERROR 400] \"page\" is out of valid range.")
    assertThat(e).isInstanceOf(CanNotGetMoreResultException::class.java)
  }

  @Test
  fun `test missed API-KEY error, returns MissedPixabayKeyException`() {
    val e = ExceptionMapper.map("[ERROR 400] Invalid or missing API key. did you add the key?")
    assertThat(e).isInstanceOf(MissedPixabayKeyException::class.java)
  }

  @Test
  fun `test unknown error, returns UnhandledException`() {
    val extraMessage = "an unknown error is happened. and we cannot handle it."
    val e = ExceptionMapper.map(extraMessage)
    assertThat(e).isInstanceOf(UnhandledException::class.java)
    assertThat(e.extraMessage).isEqualTo(extraMessage)
  }
}