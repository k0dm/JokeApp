package com.example.jokeapp.data

import com.example.jokeapp.presentation.JokeUi

interface ResultCallback {

    fun provideJoke(jokeUI: JokeUi)
    class Empty : ResultCallback {
        override fun provideJoke(jokeUI: JokeUi) = Unit
    }
}