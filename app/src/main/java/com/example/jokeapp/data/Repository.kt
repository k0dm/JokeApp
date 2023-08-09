package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.data.cache.CachedItem
import java.lang.Exception

interface Repository {

    suspend fun fetch(): CommonDataModel

    fun chooseFavorite(fromCache: Boolean)

    suspend fun changeJokeStatus(): CommonDataModel

    class Base(
        private val cloudDataSource: CloudDataSource,
        private val cacheDataSource: CacheDataSource,
        private val itemCached: CachedItem = CachedItem.Base()

    ) : Repository {

        private var getFromCache = false

        override suspend fun fetch(): CommonDataModel {

            return try {
                val jokeDataModel = if (getFromCache) {
                    cacheDataSource.fetch()
                } else {
                    cloudDataSource.fetch()
                }
                itemCached.save(jokeDataModel)
                jokeDataModel
            } catch (exception: Exception) {
                itemCached.clear()
                throw exception
            }
        }

        override fun chooseFavorite(fromCache: Boolean) {
            getFromCache = fromCache
        }

        override suspend fun changeJokeStatus(): CommonDataModel {
            return itemCached.change(cacheDataSource)
        }
    }
}