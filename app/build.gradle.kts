import com.shafaei.imageFinder.config.Dependency
import com.shafaei.imageFinder.config.Dependency.API_KEY
import com.shafaei.imageFinder.config.Dependency.App
import com.shafaei.imageFinder.config.Dependency.SdkVersions.BUILD_TOOLS_VERSION
import com.shafaei.imageFinder.config.Dependency.SdkVersions.COMPILE_SDK_VERSION
import com.shafaei.imageFinder.config.Dependency.SdkVersions.MIN_SDK_VERSION
import com.shafaei.imageFinder.config.Dependency.SdkVersions.TARGET_SDK_VERSION
import com.shafaei.imageFinder.config.Dependency.VERSION_CODE
import com.shafaei.imageFinder.config.Dependency.VERSION_NAME

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("kotlin-parcelize")
  id("dagger.hilt.android.plugin")
}

android {
  compileSdkVersion(COMPILE_SDK_VERSION)
  buildToolsVersion(BUILD_TOOLS_VERSION)

  defaultConfig {
    applicationId = "com.shafaei.imageFinder"
    minSdkVersion(MIN_SDK_VERSION)
    targetSdkVersion(TARGET_SDK_VERSION)
    versionCode = VERSION_CODE
    versionName = VERSION_NAME

    vectorDrawables.useSupportLibrary = true

    buildConfigField("String", "API_KEY", "\"$API_KEY\"")
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    getByName("debug") {
      isDebuggable = false
      isMinifyEnabled = false
    }

    getByName("release") {
      isDebuggable = false
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
  }
}

kapt {
  correctErrorTypes = true
}


dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:${Dependency.KOTLIN_VERSION}")
  implementation("androidx.core:core-ktx:1.6.0")
  implementation("androidx.appcompat:appcompat:1.3.0")
  implementation("com.google.android.material:material:1.4.0")
  implementation("androidx.recyclerview:recyclerview:1.2.1")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${App.LIFECYCLE_VERSION}")
  implementation("androidx.navigation:navigation-fragment-ktx:${App.NAV_VERSION}")
  implementation("androidx.navigation:navigation-ui-ktx:${App.NAV_VERSION}")

  // Testing dependencies
  androidTestImplementation("junit:junit:4.13.2")
  testImplementation("junit:junit:4.13.2")
  testImplementation("com.google.truth:truth:1.1.3")
  testImplementation("androidx.arch.core:core-common:2.1.0")
  testImplementation("androidx.arch.core:core-runtime:2.1.0")
  testImplementation("androidx.arch.core:core-testing:2.1.0")

  androidTestImplementation("com.google.truth:truth:1.1.3")
  testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
  androidTestImplementation("androidx.test:core-ktx:1.4.0")
  androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
  androidTestImplementation("androidx.test:rules:1.4.0")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

  // For instrumentation tests hilt
  androidTestImplementation("com.google.dagger:hilt-android-testing:${App.HILT_VERSION}")
  kaptAndroidTest("com.google.dagger:hilt-compiler:${App.HILT_VERSION}")
  // For local unit tests hilt
  testImplementation("com.google.dagger:hilt-android-testing:${App.HILT_VERSION}")
  kaptTest("com.google.dagger:hilt-compiler:${App.HILT_VERSION}")

  implementation("org.mockito:mockito-core:3.11.2")
  //    RX
  implementation("io.reactivex.rxjava2:rxkotlin:${App.RX_KOTLIN}")
  implementation("com.jakewharton.rxbinding3:rxbinding:${App.RX_BINDING}")
  implementation("com.jakewharton.rxbinding3:rxbinding-appcompat:${App.RX_BINDING}")
  implementation("com.jakewharton.rxbinding3:rxbinding-swiperefreshlayout:${App.RX_BINDING}")
  implementation("com.jakewharton.rxbinding3:rxbinding-recyclerview:${App.RX_BINDING}")

  //   Retrofit2
  implementation("com.squareup.retrofit2:retrofit:${App.RETROFIT}")
  implementation("com.squareup.retrofit2:converter-gson:${App.RETROFIT}")
  implementation("com.squareup.retrofit2:adapter-rxjava2:${App.RETROFIT}")

  implementation(platform("com.squareup.okhttp3:okhttp-bom:${App.OKHTTP}"))
  implementation("com.squareup.okhttp3:okhttp")
  implementation("com.squareup.okhttp3:logging-interceptor")

  implementation("com.github.bumptech.glide:glide:${App.GLIDE_VERSION}")
  kapt("com.github.bumptech.glide:compiler:${App.GLIDE_VERSION}")

  implementation("com.github.chrisbanes:PhotoView:2.3.0")

  implementation("com.github.Mojtaba-Shafaei:AndroidErrorMessage:1.2.7")

  // Hilt dependencies
  implementation("com.google.dagger:hilt-android:${App.HILT_VERSION}")
  kapt("com.google.dagger:hilt-android-compiler:${App.HILT_VERSION}")
}