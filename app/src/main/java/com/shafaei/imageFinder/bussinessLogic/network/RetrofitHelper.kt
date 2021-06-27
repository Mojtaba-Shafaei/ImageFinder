package com.shafaei.imageFinder.bussinessLogic.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shafaei.imageFinder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

object RetrofitHelper {
  val retrofit: Retrofit
    get() {
      val clientBuilder = OkHttpClient.Builder()
        .connectTimeout(1, MINUTES)
        .readTimeout(15, SECONDS)
        .writeTimeout(30, SECONDS)
        .retryOnConnectionFailure(true)

      val gson: Gson = GsonBuilder().create()

      // prints Http requests and response into the Log
      if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
      }

      return Retrofit.Builder()
        .baseUrl("https://pixabay.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(clientBuilder.build())
        .build()
    }
}