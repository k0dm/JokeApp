package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource

interface Repository {

    suspend fun fetch(): JokeResult

    fun chooseFavorite(fromCache: Boolean)

    suspend fun changeJokeStatus(): JokeResult

    class Base(
        private val cloudDataSource: CloudDataSource,
        private val cacheDataSource: CacheDataSource,
        private val change: Joke.Mapper<JokeResult> = Change(cacheDataSource)
    ) : Repository {

        private var jokeCached: Joke? = null
        private var getFromCache = false

        override suspend fun fetch(): JokeResult {
            val jokeResult = if (getFromCache) {
                cacheDataSource.fetch()
            } else {
                cloudDataSource.fetch()
            }

            jokeCached = if (jokeResult.isSuccessful()) {
                jokeResult.map(object : Joke.Mapper<Joke> {
                    override fun map(id: Int, text: String, punchLine: String, type: String) =
                        JokeDomain(id, text, punchLine, type)
                })
            } else {
                null
            }
            return jokeResult
        }

        override fun chooseFavorite(fromCache: Boolean) {
            getFromCache = fromCache
        }

        override suspend fun changeJokeStatus(): JokeResult {
            return jokeCached!!.map(change)
        }
    }
}