package com.example.jokeapp

import android.app.Application
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.RealmProvider
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.NewJokeService
import com.example.jokeapp.domain.JokeFailureHandler
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.domain.JokeInteractor
import com.example.jokeapp.presentation.MainViewModel
import com.example.jokeapp.presentation.State
import com.example.jokeapp.domain.StateCommunication
import io.realm.Realm
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApp : Application() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://google.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val manageResources = ManageResources.Base(this)

        mainViewModel = MainViewModel(
            JokeInteractor.Base(
                Repository.Base(
                    CloudDataSource.New(retrofit.create(NewJokeService::class.java)),
                    CacheDataSource.Base(object : RealmProvider {
                        override fun provideRealm() = Realm.getDefaultInstance()
                    })
                ),
                JokeFailureHandler.Factory(manageResources)
            ),
            StateCommunication.Base(),
            progress = State.Progress()

        )
    }
}