# ImageFinder
A Kotlin-based Android application for finding Images on [PixaBay](https://pixabay.com)

# Used Libraries and Technologies:
+ MVVM and Rx Java2 (MvRx + mvi)
+ androidx.navigation
+ Retrofit2
+ RxJava2
+ Coroutines
+ OkHttp3
+ Gson
+ Glide
+ Dependency Injection
  + Koin
  + Hilt
+ JUnit4
+ OkHttp-MockWebServer

# Branches:
+ `Master` branch: Since 31/12/2022 is deprecated and never get updates, please use one of below ones.
+ `rxjava-hilt` branch:
  + [Google-Hilt](https://dagger.dev/hilt/)
  + RxJava2
+ `rxjava-koin` branch:
  + [Koin](https://insert-koin.io/)
  + RxJava2
+ `coroutines-koin` branch:
  + [Koin](https://insert-koin.io/)
  + Kotlin Coroutines
  + Mvi Architecture
  + Junit5
  + turbine
  + mockk
  + [Kotest](https://kotest.io/)


### For Accessing to PixaBay WebServer, You Need a key.
##### Get your own from: https://pixabay.com/api/docs/
To protect my Pixabay account, I can not share my key here ;)
But to test the application, you can download the embedded apk.

And Place it in `[root project folder\buildSrc\src\main\java]\Congig.kt` Same as below:    
`const val API_KEY = "place your api key here..."`
For `rxjava-koin` and `coroutines-koin` branches put your key in the [Constants] class

### Screen
<img src="https://user-images.githubusercontent.com/12367513/126148167-c4555fa2-8418-4112-9cca-886ab8ac3dd6.png" alt="mobile1" style="max-width:30%;" width = "25%"/>

<img src="https://user-images.githubusercontent.com/12367513/126149095-d5fcc79a-6c66-4260-bd92-4c3b8bc222ee.png" alt="mobile2" style="max-width:30%;" width = "25%"/>

<img src="https://user-images.githubusercontent.com/12367513/126149349-7d380fab-74f2-483d-bf74-d8710b6c3322.png" alt="tablet1" style="max-width:50%;" width = "70%"/>

### Install
You can find and install the `.apk` file in the `apks` directory in each branch.
e.g. `[app/apks/imagefinder-rxjava-koin.apk]`

### Wiki:
https://github.com/Mojtaba-Shafaei/ImageFinder/wiki