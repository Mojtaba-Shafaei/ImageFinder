package com.shafaei.imageFinder.di

import com.shafaei.imageFinder.BuildConfig
import com.shafaei.imageFinder.businessLogic.network.service.ImageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  fun provideBaseUrl() = "https://pixabay.com/"

  @Provides
  @Singleton
  fun provideOkHttpClient() =
     OkHttpClient.Builder()
        .connectTimeout(1, MINUTES)
        .readTimeout(15, SECONDS)
        .writeTimeout(30, SECONDS)
        .retryOnConnectionFailure(true)
        .also { if (BuildConfig.DEBUG) it.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) }
        .build()

  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
     .addConverterFactory(GsonConverterFactory.create())
     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
     .baseUrl(BASE_URL)
     .client(okHttpClient)
     .build()

  @Provides
  @Singleton
  fun provideImageService(retrofit: Retrofit): ImageService = retrofit.create(ImageService::class.java)
}