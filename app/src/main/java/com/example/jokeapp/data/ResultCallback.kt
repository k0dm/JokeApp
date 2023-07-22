package com.example.jokeapp.data

import com.example.jokeapp.presentation.JokeUi

interface ResultCallback {

    fun provideJokeUi(jokeUI: JokeUi)
    class Empty : ResultCallback {
        override fun provideJokeUi(jokeUI: JokeUi) = Unit
    }
}