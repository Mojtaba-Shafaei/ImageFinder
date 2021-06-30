package com.shafaei.imageFinder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

//@Module
//@TestInstallIn(components = [SingletonComponent::class], replaces = [])
object AppModuleTest {
 // @Provides
 // @Singleton
  fun provideBaseUrl() = ""
}