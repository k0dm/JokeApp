package com.example.jokeapp.data.cache

import com.example.jokeapp.data.JokeDataModel
import com.example.jokeapp.core.Joke

interface ChangeJokeStatus {
    fun addOrRemove(id: Int, jokeDataModel: Joke): JokeDataModel
}