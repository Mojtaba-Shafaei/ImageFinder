buildscript {
  repositories {
    maven { url = uri("https://jitpack.io") }
    google()
    mavenCentral()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.2.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
    classpath("com.google.dagger:hilt-android-gradle-plugin:2.37")
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

tasks.register("deletePublishFolder", Delete::class) {
  delete("apks")
}

tasks.register("buildApp", GradleBuild::class) {
  tasks = listOf("app:assembleDebug")
}

tasks.register("publish", Copy::class) {
  from("app/build/outputs/apk/debug/")
  into("apk/")
  include("app-debug.apk")
  rename("app-debug.apk", "imageFinder.apk")

  dependsOn("buildApp")
}