package com.example.jokeapp.data.cache

import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeDomain
import com.example.jokeapp.data.JokeCallback
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToCache
import com.example.jokeapp.data.ToFavoriteUi
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.presentation.Error
import io.realm.Realm

interface CacheDataSource {

    fun addOrRemove(id: Int, joke: Joke): JokeUi

    fun fetch(jokeCallback: JokeCallback)

    class Base(manageResources: ManageResources) : CacheDataSource {
        private val noCachedJoke by lazy { Error.NoCachedJoke(manageResources) }
        override fun addOrRemove(id: Int, joke: Joke): JokeUi {
            Realm.getDefaultInstance().use {
                val jokeCache = it.where(JokeCache::class.java).equalTo("id", id).findFirst()
                if (jokeCache == null) {
                    val newJokeCache = joke.map(ToCache())
                    it.executeTransaction { realm ->
                        realm.insert(newJokeCache)
                    }
                    return joke.map(ToFavoriteUi())
                } else {
                    it.executeTransaction {
                        jokeCache.deleteFromRealm()
                    }
                    return joke.map(ToBaseUi())
                }
            }
        }

        override fun fetch(jokeCallback: JokeCallback) {
            Realm.getDefaultInstance().use {
                val jokes = it.where(JokeCache::class.java).findAll()
                if (jokes.isEmpty()) {
                    jokeCallback.provideError(noCachedJoke)
                } else {
                    val jokeCached = jokes.random()
                    jokeCallback.provideJoke(it.copyFromRealm(jokeCached))
                }
            }
        }
    }
}