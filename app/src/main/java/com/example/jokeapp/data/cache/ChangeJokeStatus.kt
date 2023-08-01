package com.example.jokeapp.data.cache

import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeResult
import com.example.jokeapp.presentation.JokeUi

interface ChangeJokeStatus {
    fun addOrRemove(id: Int, joke: Joke): JokeResult
}