package com.example.jokeapp.data.cache

import com.example.jokeapp.data.ItemDataFetcher
import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.core.DataIsFavorite
import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.core.ToCacheJoke
import com.example.jokeapp.core.ToCacheQuote
import com.example.jokeapp.core.ToDataIsFavorite
import com.example.jokeapp.core.ToDataIsNotFavorite
import com.example.jokeapp.data.cloud.JokeCloud
import com.example.jokeapp.domain.NoCachedException
import io.realm.RealmObject

interface CacheDataSource : ItemDataFetcher, ChangeItemStatus {

    abstract class Abstract<T : RealmObject>(
        private val realmProvider: RealmProvider,
        private val toCacheMapper: Mapper<T>,
        private val toDataModelIsFavorite: DataIsFavorite = ToDataIsFavorite(),
        private val toDataModelIsNotFavorite: DataIsFavorite = ToDataIsNotFavorite()
    ) : CacheDataSource {

        protected abstract val dbClass: Class<T>

        override fun addOrRemove(id: Int, commonItemDataModel: CommonItem): CommonDataModel {
            realmProvider.provideRealm().use {
                val itemCache = it.where(dbClass).equalTo("id", id).findFirst()
                if (itemCache == null) {
                    val newJokeCache = commonItemDataModel.map(toCacheMapper)
                    it.executeTransaction { realm ->
                        realm.insert(newJokeCache)
                    }
                    return commonItemDataModel.map(toDataModelIsFavorite)
                } else {
                    it.executeTransaction {
                        itemCache.deleteFromRealm()
                    }
                    return commonItemDataModel.map(toDataModelIsNotFavorite)
                }
            }
        }

        override suspend fun fetch(): CommonDataModel {
            realmProvider.provideRealm().use {
                val itemCaches = it.where(dbClass).findAll()
                if (itemCaches.isEmpty()) {
                    throw NoCachedException()
                }
                return ((it.copyFromRealm(itemCaches.random())) as CommonItem).map(
                    toDataModelIsFavorite
                )
            }
        }
    }

    class Joke(
        realmProvider: RealmProvider,
        mapper: Mapper<JokeCache> = ToCacheJoke()
    ) : Abstract<JokeCache>(realmProvider, mapper) {
        override val dbClass: Class<JokeCache>
            get() = JokeCache::class.java
    }

    class Quote(
        realmProvider: RealmProvider,
        mapper: Mapper<QuoteCache> = ToCacheQuote()
    ) : Abstract<QuoteCache>(realmProvider, mapper) {
        override val dbClass: Class<QuoteCache>
            get() = QuoteCache::class.java
    }

}