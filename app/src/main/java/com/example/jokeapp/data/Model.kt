package com.example.jokeapp.data

import com.example.jokeapp.presentation.Joke
import com.example.jokeapp.presentation.ManageResources
import com.example.jokeapp.data.cloud.JokeService
import com.example.jokeapp.presentation.Error
import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

interface Model<S, E> {

    fun getJoke()

    fun init(resultCallback: ResultCallback<S, E>)

    fun clear()

    class Base(manageResources: ManageResources, private val service: JokeService) :
        Model<Joke, Error> {
        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceUnavailable by lazy { Error.ServiceUnavailable(manageResources) }
        private var resultCallback: ResultCallback<Joke, Error> = ResultCallback.Empty()
        override fun getJoke() {

            service.getJoke().enqueue(object : retrofit2.Callback<JokeCloud> {

                override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            resultCallback.provideSuccess(body.toJoke())
                        } else {
                            resultCallback.provideError(serviceUnavailable)
                        }
                    } else {
                        resultCallback.provideError(serviceUnavailable)
                    }
                }

                override fun onFailure(call: Call<JokeCloud>, t: Throwable) {
                    val error = if (t is UnknownHostException) {
                        noConnection
                    } else {
                        serviceUnavailable
                    }
                    resultCallback.provideError(error)
                }
            })
        }

        override fun clear() {
            resultCallback = ResultCallback.Empty()
        }

        override fun init(resultCallback: ResultCallback<Joke, Error>) {
            this.resultCallback = resultCallback
        }

    }

    class Test(manageResources: ManageResources) : Model<Joke, Error> {

        private val noConnection = Error.NoConnection(manageResources)
        private val serviceUnavailable = Error.ServiceUnavailable(manageResources)
        private var resultCallback: ResultCallback<Joke, Error> = ResultCallback.Empty()
        private var counter = 0

        override fun getJoke() {
            Thread {
                Thread.sleep(1000L)

                if (++counter % 3 == 1) {
                    resultCallback.provideSuccess(Joke("testText", "testPunchLine"))
                } else if (counter == 2) {
                    resultCallback.provideError(noConnection)
                } else {
                    counter = 0
                    resultCallback.provideError(serviceUnavailable)
                }
            }.start()

        }

        override fun clear() {
            resultCallback = ResultCallback.Empty()
        }

        override fun init(resultCallback: ResultCallback<Joke, Error>) {
            this.resultCallback = resultCallback
        }

    }
}