package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.JokeCallback
import com.example.jokeapp.presentation.Error
import com.example.jokeapp.presentation.ManageResources
import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

interface CloudDataSource {

    fun fetch(jokeCallback: JokeCallback)

    class Base(
        private val service: JokeService,
        manageResources: ManageResources
    ) : CloudDataSource {

        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceUnavailable by lazy { Error.ServiceUnavailable(manageResources) }
        override fun fetch(jokeCallback: JokeCallback) {
            service.getJoke().enqueue(object : retrofit2.Callback<JokeCloud> {

                override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            jokeCallback.provideJoke(body)
                        } else {
                            jokeCallback.provideError(serviceUnavailable)
                        }
                    } else {
                        jokeCallback.provideError(serviceUnavailable)
                    }
                }

                override fun onFailure(call: Call<JokeCloud>, t: Throwable) {
                    val error = if (t is UnknownHostException) {
                        noConnection
                    } else {
                        serviceUnavailable
                    }
                    jokeCallback.provideError(error)
                }
            })
        }
    }
}