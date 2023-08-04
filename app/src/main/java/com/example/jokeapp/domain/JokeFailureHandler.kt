package com.example.jokeapp.domain

import com.example.jokeapp.presentation.Error
import com.example.jokeapp.presentation.ManageResources

interface JokeFailureHandler {

    fun handle(e: Exception): Error

    class Factory(private val manageResources: ManageResources) : JokeFailureHandler {

        private val noConnection by lazy { Error.NoConnection(manageResources) }
        private val serviceUnavailable by lazy { Error.ServiceUnavailable(manageResources) }
        private val noCachedJoke by lazy { Error.NoCachedJoke(manageResources) }
        private val genericError by lazy { Error.GenericError(manageResources) }

        override fun handle(e: Exception): Error {
            return when (e) {
                is NoConnectionException -> noConnection
                is ServiceUnavailableException -> serviceUnavailable
                is NoCachedException -> noCachedJoke
                else -> genericError
            }
        }
    }
}