package com.example.jokeapp.sl

import com.example.jokeapp.core.data.cache.RealmProvider
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.NewJokeService
import com.example.jokeapp.domain.CommonInteractor
import com.example.jokeapp.domain.FailureHandler
import com.example.jokeapp.presentation.StateCommunication
import com.example.jokeapp.presentation.joke.JokesViewModel
import retrofit2.Retrofit

class JokesModule(
    private val failureHandler: FailureHandler,
    private val realmProvider: RealmProvider,
    private val retrofit: Retrofit
) : Module.Base<Int, JokesViewModel>() {

    private var communication: StateCommunication<Int>? = null

    override fun getViewModel() = JokesViewModel(
        CommonInteractor.Base(
            Repository.Base(
                CloudDataSource.NewJoke(retrofit.create(NewJokeService::class.java)),
                CacheDataSource.Joke(realmProvider)
            ),
            failureHandler
        ),
        getCommunication()
    )


    override fun getCommunication(): StateCommunication<Int> {
        if (communication == null) {
            communication = StateCommunication.Base()
        }
        return communication!!
    }
}