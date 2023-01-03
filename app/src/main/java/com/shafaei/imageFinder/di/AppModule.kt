package com.shafaei.imageFinder.di

import com.shafaei.imageFinder.BuildConfig
import com.shafaei.imageFinder.businessLogic.ImageApi
import com.shafaei.imageFinder.businessLogic.network.NetworkImageApi
import com.shafaei.imageFinder.businessLogic.network.service.ImageService
import com.shafaei.imageFinder.ui.list.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

fun getModules(): List<Module> = listOf(
   baseUrl, httpClient, retrofit,
   imageService,
   imageApi,
   listRepo,
   listViewModel
)

///////////////////////////////////////////////////////////////////////////////////////////////////
val baseUrl = module { single<String>(named("base_url")) { "https://pixabay.com/" } }
val httpClient = module {
  single<OkHttpClient> {
    OkHttpClient.Builder()
       .connectTimeout(1, MINUTES)
       .readTimeout(15, SECONDS)
       .writeTimeout(30, SECONDS)
       .retryOnConnectionFailure(true)
       .also { if (BuildConfig.DEBUG) it.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) }
       .build()
  }
}
val retrofit = module {
  single<Retrofit> {
    Retrofit.Builder()
       .addConverterFactory(GsonConverterFactory.create())
       .baseUrl(get<String>(named("base_url")))
       .client(get())
       .build()
  }
}
val imageService = module { single<ImageService> { get<Retrofit>().create(ImageService::class.java) } }
val imageApi = module { single<ImageApi> { NetworkImageApi(imageService = get<ImageService>()) } }
val listRepo = module { factory<ListRepository> { ListRepositoryImpl(imageApi = get()) } }
val listViewModel = module { viewModel { ListViewModel(mRepo = get()) } }
