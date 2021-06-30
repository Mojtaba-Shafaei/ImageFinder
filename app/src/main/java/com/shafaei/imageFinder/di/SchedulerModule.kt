package com.shafaei.imageFinder.di

import com.shafaei.imageFinder.rx.DefaultRxSchedulers
import com.shafaei.imageFinder.rx.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {
  @Binds
  abstract fun provideSchedulers(api: DefaultRxSchedulers): SchedulerProvider
}