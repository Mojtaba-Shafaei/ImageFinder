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
   
And Place it in `[root project folder]\gradle.properties` Same as below:    
`API_KEY="paste the given key here"`
