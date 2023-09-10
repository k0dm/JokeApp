package com.example.jokeapp.data.cache

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.core.ToCacheJoke
import com.example.jokeapp.core.ToCacheQuote
import com.example.jokeapp.core.ToDataIsFavorite
import com.example.jokeapp.core.ToDataIsNotFavorite
import com.example.jokeapp.core.data.cache.RealmProvider
import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.data.DataFetcher
import com.example.jokeapp.domain.NoCachedException
import io.realm.Realm
import io.realm.RealmObject

interface CacheDataSource<E, T> : DataFetcher<E>, ChangeItemStatus<E> {

    suspend fun fetchDataList(): List<CommonDataModel<E>>

    suspend fun delete(id: E) : Boolean

    abstract class Abstract<E, T : RealmObject>(
        private val realmProvider: RealmProvider,
        private val toCacheMapper: Mapper<E, T>,
        private val toDataModelIsFavorite: ToDataIsFavorite<E> = ToDataIsFavorite(),
        private val toDataModelIsNotFavorite: ToDataIsNotFavorite<E> = ToDataIsNotFavorite()
    ) : CacheDataSource<E, T> {

        protected abstract val dbClass: Class<T>
        protected abstract fun findRealObject(realm: Realm, id: E): T?

        override fun addOrRemove(id: E, commonItemDataModel: CommonItem<E>): CommonDataModel<E> {
            realmProvider.provideRealm().use {
                val itemCache = findRealObject(it, id)
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

        override suspend fun delete(id: E): Boolean {
            realmProvider.provideRealm().use {
                val item = findRealObject(it, id)
                return if (item == null) {
                    false
                }else {
                    it.executeTransaction{
                        item.deleteFromRealm()
                    }
                    true
                }
            }
        }

        override suspend fun fetch() = getRealmData().random().map(toDataModelIsFavorite)

        override suspend fun fetchDataList() = getRealmData().map { it.map(toDataModelIsFavorite) }

        private fun getRealmData(): List<CommonItem<E>> {
            realmProvider.provideRealm().use { realm ->
                val itemCaches = realm.where(dbClass).findAll()
                if (itemCaches.isEmpty()) {
                    throw NoCachedException()
                }
                val list = arrayListOf<CommonItem<E>>()
                itemCaches.map { item ->
                    list.add(realm.copyFromRealm(item) as CommonItem<E>)
                }
                return list
            }
        }
    }

    class Joke(
        realmProvider: RealmProvider,
        mapper: Mapper<Int, JokeCache> = ToCacheJoke()
    ) : Abstract<Int, JokeCache>(realmProvider, mapper) {
        override val dbClass: Class<JokeCache>
            get() = JokeCache::class.java

        override fun findRealObject(realm: Realm, id: Int): JokeCache? {
            return realm.where(dbClass).equalTo("id", id).findFirst()
        }
    }

    class Quote(
        realmProvider: RealmProvider,
        mapper: Mapper<String, QuoteCache> = ToCacheQuote()
    ) : Abstract<String, QuoteCache>(realmProvider, mapper) {
        override val dbClass: Class<QuoteCache>
            get() = QuoteCache::class.java

        override fun findRealObject(realm: Realm, id: String): QuoteCache? {
            return realm.where(dbClass).equalTo("id", id).findFirst()
        }
    }

}