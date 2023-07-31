package com.example.jokeapp.data.cache

import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeDataFetcher
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToCache
import com.example.jokeapp.data.ToFavoriteUi
import com.example.jokeapp.presentation.JokeUi
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.presentation.Error

interface CacheDataSource: JokeDataFetcher<JokeResult>, ChangeJokeStatus {

    class Base(private val realmProvider: RealmProvider, manageResources: ManageResources) : CacheDataSource {
        private val noCachedJoke by lazy { Error.NoCachedJoke(manageResources) }
        override fun addOrRemove(id: Int, joke: Joke): JokeUi {
           realmProvider.provideRealm().use {
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

        override fun fetch(): JokeResult{
            realmProvider.provideRealm().use {
                val jokes = it.where(JokeCache::class.java).findAll()
                if (jokes.isEmpty()) {
                    return JokeResult.Failure(noCachedJoke)
                } else {
                    val jokeCached = jokes.random()
                    return JokeResult.Success(it.copyFromRealm(jokeCached), true            )
                }
            }
        }
    }
}