package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeDataFetcher
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.presentation.Error
import com.example.jokeapp.presentation.ManageResources
import retrofit2.Call

import java.lang.Exception
import java.net.UnknownHostException

interface CloudDataSource : JokeDataFetcher<JokeResult> {

    abstract class Abstract<T>(manageResources: ManageResources) : CloudDataSource {

        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceUnavailable by lazy { Error.ServiceUnavailable(manageResources) }

        protected abstract fun getJokeCloud(): Call<T>
        override suspend fun fetch(): JokeResult {
            return try {
                val response = getJokeCloud().execute()
                JokeResult.Success(response.body()!! as Joke, false)
            } catch (e: Exception) {
                val error = if (e is UnknownHostException) {
                    noConnection
                } else {
                    serviceUnavailable
                }
                JokeResult.Failure(error)
            }
        }
    }

    class Base(private val service: BaseJokeService, manageResources: ManageResources) :
        Abstract<JokeCloud>(manageResources) {
        override fun getJokeCloud() = service.getJoke()
    }

    class New(private val service: NewJokeService, manageResources: ManageResources) :
        Abstract<NewJokeCloud>(manageResources) {
        override fun getJokeCloud() = service.getJoke()

    }
}