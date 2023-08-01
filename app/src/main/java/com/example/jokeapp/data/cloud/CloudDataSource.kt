package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.JokeDataFetcher
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.presentation.Error
import com.example.jokeapp.presentation.ManageResources

import java.lang.Exception
import java.net.UnknownHostException

interface CloudDataSource: JokeDataFetcher<JokeResult> {

    class Base(
        private val service: JokeService,
        manageResources: ManageResources
    ) : CloudDataSource {

        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceUnavailable by lazy { Error.ServiceUnavailable(manageResources) }
        override fun fetch(): JokeResult {
            return try {
                val response = service.getJoke().execute()
                JokeResult.Success(response.body()!!, false)
            }catch (e: Exception) {
                val error = if (e is UnknownHostException) {
                    noConnection
                } else {
                    serviceUnavailable
                }
                JokeResult.Failure(error)
            }
        }
    }
}