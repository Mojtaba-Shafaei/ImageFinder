package com.shafaei.imageFinder.rx

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ImmediateSchedulersRule : TestRule {
  private val testScheduler = Schedulers.single()

  override fun apply(base: Statement, description: Description): Statement {
    return object : Statement() {
      override fun evaluate() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
        RxJavaPlugins.setSingleSchedulerHandler { testScheduler }

        try {
          base.evaluate()
        }catch (e:Exception){
          println(e)
        }
        finally {
          RxJavaPlugins.reset()
        }
      }
    }
  }
}