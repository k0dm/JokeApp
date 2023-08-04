package com.example.jokeapp.data.cache

import com.example.jokeapp.data.JokeDataModel

interface CachedJoke : ChangeJoke {

    fun save(jokeDataModel: JokeDataModel)
    fun clear()

    class Base() : CachedJoke {
        private var joke: ChangeJoke = ChangeJoke.Empty()

        override fun save(jokeDataModel: JokeDataModel) {
            joke = jokeDataModel
        }

        override fun clear() {
            joke =  ChangeJoke.Empty()
        }

        override suspend fun change(changeJokeStatus: ChangeJokeStatus): JokeDataModel {
            return joke.change(changeJokeStatus)
        }
    }
}