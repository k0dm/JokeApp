package com.example.jokeapp.data.cache

import com.example.jokeapp.data.JokeDataFetcher
import com.example.jokeapp.data.JokeDataModel
import com.example.jokeapp.core.DataIsFavorite
import com.example.jokeapp.core.Joke
import com.example.jokeapp.core.ToCache
import com.example.jokeapp.core.ToDataIsFavorite
import com.example.jokeapp.core.ToDataIsNotFavorite
import com.example.jokeapp.domain.NoCachedException

interface CacheDataSource : JokeDataFetcher, ChangeJokeStatus {

    class Base(
        private val realmProvider: RealmProvider,
        private val toCacheMapper: ToCache = ToCache(),
        private val toDataModelIsFavorite: DataIsFavorite = ToDataIsFavorite(),
        private val toDataModelIsNotFavorite: DataIsFavorite = ToDataIsNotFavorite(),

        ) : CacheDataSource {

        override fun addOrRemove(id: Int, joke: Joke): JokeDataModel {
            realmProvider.provideRealm().use {
                val jokeCache = it.where(JokeCache::class.java).equalTo("id", id).findFirst()
                if (jokeCache == null) {
                    val newJokeCache = joke.map(toCacheMapper)
                    it.executeTransaction { realm ->
                        realm.insert(newJokeCache)
                    }
                    return joke.map(toDataModelIsFavorite)
                } else {
                    it.executeTransaction {
                        jokeCache.deleteFromRealm()
                    }
                    return joke.map(toDataModelIsNotFavorite)
                }
            }
        }

        override suspend fun fetch(): JokeDataModel {
            realmProvider.provideRealm().use {
                val jokes = it.where(JokeCache::class.java).findAll()
                if (jokes.isEmpty()) {
                    throw NoCachedException()
                }
                return it.copyFromRealm(jokes.random()).map(toDataModelIsFavorite)
            }
        }
    }
}