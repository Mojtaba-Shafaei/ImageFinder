# ImageFinder
A Kotlin-based Android application for finding Images on [PixaBay](https://pixabay.com)

# Used Libraries and Technologies:
+ MVVM and Rx Java2 (MvRx + mvi)
+ androidx.navigation
+ Retrofit2
+ RxJava2
+ OkHttp3
+ Gson
+ Glide
+ Dependency Injection
   + in `Master` branch: [Google-Hilt](https://dagger.dev/hilt/)
   + in `Koin` branch: [Koin](https://insert-koin.io/)
+ JUnit4
+ OkHttp-MockWebServer



### For Accessing to PixaBay WebServer, You Need a key.
##### Get it From: https://pixabay.com/api/docs/    
   
And Place it in `[root project folder\buildSrc\src\main\java]\Congig.kt` Same as below:    
`const val API_KEY = "place your api key here..."`


### Screen
<img src="https://user-images.githubusercontent.com/12367513/126148167-c4555fa2-8418-4112-9cca-886ab8ac3dd6.png" alt="mobile1" style="max-width:30%;" width = "25%"/>

<img src="https://user-images.githubusercontent.com/12367513/126149095-d5fcc79a-6c66-4260-bd92-4c3b8bc222ee.png" alt="mobile2" style="max-width:30%;" width = "25%"/>

<img src="https://user-images.githubusercontent.com/12367513/126149349-7d380fab-74f2-483d-bf74-d8710b6c3322.png" alt="tablet1" style="max-width:50%;" width = "70%"/>

### Install 
Since version `1.1.0` the `.apk` file has added to the project. You can find and install it in 
`[root project folder]\apk\imageFinder.apk`
