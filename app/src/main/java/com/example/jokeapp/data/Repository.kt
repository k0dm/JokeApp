package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.JokeCacheCallback
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cloud.JokeCloudCallback
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
        private val cacheDataSource: CacheDataSource
    ) : Repository {

        private var resultCallback: ResultCallback = ResultCallback.Empty()
        private var jokeCached: JokeCloud? = null
        private var getFromCache = false

        override fun getJoke() {
            if (getFromCache) {
                cacheDataSource.fetch(object : JokeCacheCallback{
                    override fun provideJoke(jokeCloud: JokeCloud) {
                        jokeCached = jokeCloud
                        resultCallback.provideJoke(jokeCloud.toFavoriteJoke())
                    }

                    override fun provideError(error: Error) {
                        jokeCached = null
                        resultCallback.provideJoke(JokeUi.Failed(error.message()))
                    }
                })
            } else {
                cloudDataSource.fetch(object : JokeCloudCallback {
                    override fun provideJokeCloud(jokeCloud: JokeCloud) {
                        jokeCached = jokeCloud
                        resultCallback.provideJoke(jokeCloud.toBaseJoke())
                    }

                    override fun provideError(error: Error) {
                        resultCallback.provideJoke(JokeUi.Failed(error.message()))
                    }
                })
            }

        }

        override fun chooseFavorite(fromCache: Boolean) {
            getFromCache = fromCache
        }

        override fun changeJokeStatus(resultCallback: ResultCallback) {
            jokeCached?.change(cacheDataSource)?.let {
                resultCallback.provideJoke(it)
            }
        }

        override fun clear() {
            resultCallback = ResultCallback.Empty()
        }

        override fun init(resultCallback: ResultCallback) {
            this.resultCallback = resultCallback
        }

    }

    class Test(manageResources: ManageResources) : Repository {

        private val noConnection = Error.NoConnection(manageResources)
        private val serviceUnavailable = Error.ServiceUnavailable(manageResources)
        private var resultCallback: ResultCallback = ResultCallback.Empty()
        private var counter = 0

        override fun getJoke() {
            Thread {
                Thread.sleep(1000L)

                if (++counter % 3 == 1) {
                    resultCallback.provideJoke(JokeUi.Base("testText", "testPunchLine"))
                } else if (counter == 2) {
                    resultCallback.provideJoke(
                        JokeUi.Favorite(
                            "favorite text", "favorite punchline"
                        )
                    )
                } else {
                    counter = 0
                    resultCallback.provideJoke(JokeUi.Failed(serviceUnavailable.message()))
                }
            }.start()

        }

        override fun chooseFavorite(fromCache: Boolean) {
            TODO("Not yet implemented")
        }

        override fun changeJokeStatus(resultCallback: ResultCallback) {
            TODO("Not yet implemented")
        }

        override fun clear() {
            resultCallback = ResultCallback.Empty()
        }

        override fun init(resultCallback: ResultCallback) {
            this.resultCallback = resultCallback
        }

    }
}