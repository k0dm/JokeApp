package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.ChangeJoke
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.core.Change
import com.example.jokeapp.data.cache.CachedJoke
import com.example.jokeapp.data.cache.JokeCache
import java.lang.Exception

interface Repository {

    suspend fun fetch(): JokeDataModel

    fun chooseFavorite(fromCache: Boolean)

    suspend fun changeJokeStatus(): JokeDataModel

    class Base(
        private val cloudDataSource: CloudDataSource,
        private val cacheDataSource: CacheDataSource,
        private val jokeCached: CachedJoke = CachedJoke.Base()

    ) : Repository {

        private var getFromCache = false

        override suspend fun fetch(): JokeDataModel {

            return try {
                val jokeDataModel = if (getFromCache) {
                    cacheDataSource.fetch()
                } else {
                    cloudDataSource.fetch()
                }
                jokeCached.save(jokeDataModel)
                jokeDataModel
            } catch (exception: Exception) {
                jokeCached.clear()
                throw exception
            }
        }

        override fun chooseFavorite(fromCache: Boolean) {
            getFromCache = fromCache
        }

        override suspend fun changeJokeStatus(): JokeDataModel {
            return jokeCached.change(cacheDataSource)
        }
    }
}