package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.JokeDataFetcher
import com.example.jokeapp.data.JokeDataModel
import com.example.jokeapp.core.Joke
import com.example.jokeapp.core.ToDataIsNotFavorite
import com.example.jokeapp.domain.NoConnectionException
import com.example.jokeapp.domain.ServiceUnavailableException
import retrofit2.Call
import java.lang.Exception
import java.net.UnknownHostException

interface CloudDataSource : JokeDataFetcher {

    abstract class Abstract<T: Joke>(private val mapper: ToDataIsNotFavorite) : CloudDataSource {

        protected abstract fun getJokeCloud(): Call<T>
        override suspend fun fetch(): JokeDataModel {
            try{
                val responseBody = getJokeCloud().execute().body()
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

    class Base(private val service: BaseJokeService) : Abstract<JokeCloud>(ToDataIsNotFavorite()) {
        override fun getJokeCloud() = service.getJoke()
    }

    class New(private val service: NewJokeService) : Abstract<NewJokeCloud>(ToDataIsNotFavorite()) {
        override fun getJokeCloud() = service.getJoke()
    }
}