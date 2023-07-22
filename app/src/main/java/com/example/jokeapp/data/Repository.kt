package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.presentation.Error

interface Repository {

    fun getJoke()

    fun chooseFavorite(fromCache: Boolean)

    fun changeJokeStatus(resultCallback: ResultCallback)

    fun init(resultCallback: ResultCallback)

    fun clear()

    class Base(
        private val cloudDataSource: CloudDataSource,
        private val cacheDataSource: CacheDataSource,
        private val toBaseUi: ToBaseUi = ToBaseUi(),
        private val toFavoriteUi: ToFavoriteUi = ToFavoriteUi()
    ) : Repository {

        private var resultCallback: ResultCallback = ResultCallback.Empty()
        private var jokeCached: Joke? = null
        private var getFromCache = false

        private inner class BaseJokeCallback(private val mapper: Joke.Mapper<JokeUi>) : JokeCallback {
            override fun provideJoke(joke: Joke) {
                jokeCached = joke
                resultCallback.provideJokeUi(joke.map(mapper))
            }

            override fun provideError(error: Error) {
                jokeCached = null
                resultCallback.provideJokeUi(JokeUi.Failed(error.message()))
            }
        }

        override fun getJoke() {
            if (getFromCache) {
                cacheDataSource.fetch(BaseJokeCallback(toFavoriteUi))
            } else {
                cloudDataSource.fetch(BaseJokeCallback(toBaseUi))
            }
        }

        override fun chooseFavorite(fromCache: Boolean) {
            getFromCache = fromCache
        }

        override fun changeJokeStatus(resultCallback: ResultCallback) {
            jokeCached?.let {
                resultCallback.provideJokeUi(it.map(Change(cacheDataSource)))
            }
        }

        override fun clear() {
            resultCallback = ResultCallback.Empty()
        }

        override fun init(resultCallback: ResultCallback) {
            this.resultCallback = resultCallback
        }

    }
}