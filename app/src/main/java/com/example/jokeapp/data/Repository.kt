package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cache.CachedItem
import com.example.jokeapp.data.cloud.CloudDataSource

interface Repository<E> {

    suspend fun fetch(): CommonDataModel<E>
    suspend fun fetchDataList(): List<CommonDataModel<E>>

    suspend fun delete(id: E)

    fun chooseFavorite(fromCache: Boolean)

    suspend fun changeJokeStatus(): CommonDataModel<E>

    class Base<E, T>(
        private val cloudDataSource: CloudDataSource<E>,
        private val cacheDataSource: CacheDataSource<E, T>,
        private val itemCached: CachedItem<E> = CachedItem.Base()

    ) : Repository<E> {

        private var getFromCache = false

        override suspend fun fetch(): CommonDataModel<E> {

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

        override suspend fun fetchDataList() = cacheDataSource.fetchDataList()
        override suspend fun delete(id: E) {
            cacheDataSource.delete(id)
        }

        override fun chooseFavorite(fromCache: Boolean) {
            getFromCache = fromCache
        }

        override suspend fun changeJokeStatus(): CommonDataModel<E> {
            return itemCached.change(cacheDataSource)
        }

    }
}