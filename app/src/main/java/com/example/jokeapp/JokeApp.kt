package com.example.jokeapp

import android.app.Application
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.RealmProvider
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.data.cloud.JokeService
import com.example.jokeapp.presentation.Communication
import com.example.jokeapp.presentation.JokeCommunication
import com.example.jokeapp.presentation.MainViewModel
import io.realm.Realm
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JokeApp : Application() {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val manageResources = ManageResources.Base(this)
        mainViewModel = MainViewModel(
            JokeCommunication.Base(),
            Repository.Base(
                CloudDataSource.Base(
                    retrofit.create(JokeService::class.java),
                    manageResources
                ),
                CacheDataSource.Base(object : RealmProvider {
                    override fun provideRealm() = Realm.getDefaultInstance()
                }, manageResources)
            )
        )
    }
}