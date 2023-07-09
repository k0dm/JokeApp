package com.example.jokeapp

import android.widget.HeterogeneousExpandableList
import com.example.jokeapp.data.cloud.ErrorType
import com.example.jokeapp.data.cloud.JokeService
import com.example.jokeapp.data.cloud.ServiceCallback

interface Model<S, E> {

    fun getJoke()

    fun init(resultCallback: ResultCallback<S, E>)

    fun clear()

    class Base(manageResources: ManageResources, private val service: JokeService) : Model<Joke, Error> {
        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceUnavailable by lazy { Error.ServiceUnavailable(manageResources) }
        private var resultCallback: ResultCallback<Joke, Error> = ResultCallback.Empty()
        override fun getJoke() {
            service.getJoke(object : ServiceCallback{

                override fun returnSuccess(data: String) {
                    resultCallback.provideSuccess(Joke(data,""))
                }

                override fun returnError(type: ErrorType) {
                    val error = if (type == ErrorType.NO_INTERNET) {
                        noConnection
                    }else {
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