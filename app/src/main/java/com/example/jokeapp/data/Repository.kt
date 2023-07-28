package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.data.cloud.CloudDataSource
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.Error

interface Repository {

    fun fetch(): JokeResult

    fun chooseFavorite(fromCache: Boolean)

    fun changeJokeStatus(): JokeUi


    class Base(
        private val cloudDataSource: CloudDataSource,
        private val cacheDataSource: CacheDataSource,
        private val change: Joke.Mapper<JokeUi> = Change(cacheDataSource)
    ) : Repository {

        private var jokeCached: Joke? = null
        private var getFromCache = false

        override fun fetch(): JokeResult {
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

        override fun changeJokeStatus(): JokeUi{
            return jokeCached!!.map(change)
        }
    }
}