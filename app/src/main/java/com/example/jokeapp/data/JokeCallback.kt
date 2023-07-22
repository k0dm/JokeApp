package com.example.jokeapp.data

import com.example.jokeapp.presentation.Error

interface JokeCallback {
    fun provideJoke(joke: Joke)

    fun provideError(error: Error)
}