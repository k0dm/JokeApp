package com.example.jokeapp.data.cache

import com.example.jokeapp.data.JokeCloud
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.presentation.Error
import io.realm.Realm

interface CacheDataSource {

    fun addOrRemove(id: Int, joke: JokeCloud): JokeUi

    fun fetch(jokeCacheCallback: JokeCacheCallback)

    class Base(private val realm: Realm, manageResources: ManageResources) : CacheDataSource {
        private val noCachedJoke by lazy { Error.NoCachedJoke(manageResources) }
        override fun addOrRemove(id: Int, joke: JokeCloud): JokeUi {
            Realm.getDefaultInstance().use {
                val jokeCache = it.where(JokeCache::class.java).equalTo("id", id).findFirst()
                if (jokeCache == null) {
                    val newJokeCache = joke.toCacheJoke()
                    it.executeTransaction { realm ->
                        realm.insert(newJokeCache)
                    }
                    return joke.toFavoriteJoke()
                } else {
                    it.executeTransaction {
                        jokeCache.deleteFromRealm()
                    }
                    return joke.toBaseJoke()
                }
            }
        }

        override fun fetch(jokeCacheCallback: JokeCacheCallback) {
            Realm.getDefaultInstance().use {
                val jokes = it.where(JokeCache::class.java).findAll()
                if (jokes.isEmpty()) {
                    jokeCacheCallback.provideError(noCachedJoke)
                } else {
                    val jokeCached = jokes.random()
                    jokeCacheCallback.provideJoke(
                        JokeCloud(
                            jokeCached.id,
                            jokeCached.text,
                            jokeCached.punchline,
                            jokeCached.type
                        )
                    )
                }
            }
        }

    }

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
            } else {
                if (map.size >= count) {
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