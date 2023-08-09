package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.ItemDataFetcher
import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.ToDataIsNotFavorite
import com.example.jokeapp.domain.NoConnectionException
import com.example.jokeapp.domain.ServiceUnavailableException
import retrofit2.Call
import java.lang.Exception
import java.net.UnknownHostException

interface CloudDataSource : ItemDataFetcher {

    abstract class Abstract<T: CommonItem>(private val mapper: ToDataIsNotFavorite) : CloudDataSource {

        protected abstract fun getItemCloud(): Call<T>
        override suspend fun fetch(): CommonDataModel {
            try{
                val responseBody = getItemCloud().execute().body()
                return responseBody!!.map(mapper)
            }catch (e: Exception) {
                if (e is UnknownHostException) {
                    throw NoConnectionException()
                }else {
                    throw ServiceUnavailableException()
                }
            }
        }
    }

    class BaseJoke(private val service: BaseJokeService) : Abstract<JokeCloud>(ToDataIsNotFavorite()) {
        override fun getItemCloud() = service.fetch()
    }

    class NewJoke(private val service: NewJokeService) : Abstract<NewJokeCloud>(ToDataIsNotFavorite()) {
        override fun getItemCloud() = service.fetch()
    }

    class Quote(private val service: QuoteService) : Abstract<QuoteCloud>(ToDataIsNotFavorite()) {
        override fun getItemCloud(): Call<QuoteCloud> = service.fetch()
    }
}