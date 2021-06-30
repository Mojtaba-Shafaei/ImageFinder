package com.shafaei.imageFinder.di

import com.shafaei.imageFinder.businessLogic.ImageApi
import com.shafaei.imageFinder.businessLogic.network.NetworkImageApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class NetworkImageApiModule {
  @Binds
  abstract fun provideImageApi(api: NetworkImageApi): ImageApi
}