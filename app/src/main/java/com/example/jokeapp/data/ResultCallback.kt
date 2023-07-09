package com.example.jokeapp.data

import com.example.jokeapp.presentation.Joke
import com.example.jokeapp.presentation.Error

interface ResultCallback<S, E> {

    fun provideSuccess(data: S)

    fun provideError(error: E)

    class Empty : ResultCallback<Joke, Error> {
        override fun provideSuccess(data: Joke) = Unit

        override fun provideError(error: Error) = Unit
    }
}