package com.example.jokeapp.data.cache

import com.example.jokeapp.data.JokeCloud
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.presentation.Error

interface CacheDataSource {

    fun addOrRemove(id: Int, joke: JokeCloud): JokeUi

    fun fetch(jokeCacheCallback: JokeCacheCallback)

    class Fake(manageResources: ManageResources) : CacheDataSource {

        private val noCachedJoke by lazy { Error.NoCachedJoke(manageResources) }
        private val map = HashMap<Int, JokeCloud>()
        private var count = 0

        override fun addOrRemove(id: Int, joke: JokeCloud): JokeUi {
            return if (map[id] != null) {
                map.remove(id)
                joke.toBaseJoke()
            } else {
                map[id] = joke
                joke.toFavoriteJoke()
            }
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            if (map.isEmpty()) {
                jokeCacheCallback.provideError(noCachedJoke)
            } else{
                if (map.size >= count){
                    count = 0
                }
                val jokeCloud = map.toList()[count++].second

                jokeCacheCallback.provideJoke(jokeCloud)
            }
        }
    }
}

interface JokeCacheCallback {

    fun provideJoke(jokeCloud: JokeCloud)

    fun provideError(error: Error)
}