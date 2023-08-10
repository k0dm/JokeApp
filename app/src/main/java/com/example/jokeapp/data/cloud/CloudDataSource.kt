package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.ToDataIsNotFavorite
import com.example.jokeapp.data.DataFetcher
import com.example.jokeapp.domain.NoConnectionException
import com.example.jokeapp.domain.ServiceUnavailableException
import retrofit2.Call
import java.lang.Exception
import java.net.UnknownHostException

interface CloudDataSource<E> : DataFetcher<E> {

    abstract class Abstract<E, T : CommonItem<E>>(private val mapper: ToDataIsNotFavorite<E>) :
        CloudDataSource<E> {

        protected abstract fun getItemCloud(): Call<T>
        override suspend fun fetch(): CommonDataModel<E> {
            try {
                val responseBody = getItemCloud().execute().body()
                return responseBody!!.map(mapper)
            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    throw NoConnectionException()
                } else {
                    throw ServiceUnavailableException()
                }
            }
        }
    }

    class BaseJoke(private val service: BaseJokeService) :
        Abstract<Int, JokeCloud>(ToDataIsNotFavorite()) {

        override fun getItemCloud() = service.fetch()
    }

    class NewJoke(private val service: NewJokeService) :
        Abstract<Int, NewJokeCloud>(ToDataIsNotFavorite()) {
        override fun getItemCloud() = service.fetch()
    }

    class Quote(private val service: QuoteService) :
        Abstract<String, QuoteCloud>(ToDataIsNotFavorite()) {
        override fun getItemCloud(): Call<QuoteCloud> = service.fetch()
    }
}