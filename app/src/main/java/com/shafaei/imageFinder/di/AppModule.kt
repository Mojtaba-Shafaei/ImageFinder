package com.shafaei.imageFinder.di

import com.shafaei.imageFinder.BuildConfig
import com.shafaei.imageFinder.businessLogic.ImageApi
import com.shafaei.imageFinder.businessLogic.network.NetworkImageApi
import com.shafaei.imageFinder.businessLogic.network.service.ImageService
import com.shafaei.imageFinder.ui.list.ListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

val appModules = module {
  single<String>(named("base_url")) { "https://pixabay.com/" }

  single<OkHttpClient> {
    OkHttpClient.Builder()
       .connectTimeout(1, MINUTES)
       .readTimeout(15, SECONDS)
       .writeTimeout(30, SECONDS)
       .retryOnConnectionFailure(true)
       .also { if (BuildConfig.DEBUG) it.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) }
       .build()
  }

  single<Retrofit> {
    Retrofit.Builder()
       .addConverterFactory(GsonConverterFactory.create())
       .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
       .baseUrl(get<String>(named("base_url")))
       .client(get())
       .build()
  }

  single<ImageService> { get<Retrofit>().create(ImageService::class.java) }

  single<ImageApi> { NetworkImageApi(imageService = get<ImageService>()) as ImageApi }

  viewModel<ListViewModel> { ListViewModel(imageApi = get()) }
}