package com.example.jokeapp.data

import com.example.jokeapp.presentation.Error
import java.lang.IllegalStateException

interface JokeResult : Joke {

    fun isFavorite(): Boolean
    fun isSuccessful(): Boolean
    fun errorMessage(): String

    class Success(private val joke: Joke, private val isFavorite: Boolean) : JokeResult {
        override fun isFavorite() = isFavorite
        override fun isSuccessful() = true
        override fun errorMessage() = ""
        override fun <T> map(mapper: Joke.Mapper<T>) = joke.map(mapper)
    }

    class Failure(private val error: Error) : JokeResult {
        override fun isFavorite() = false
        override fun isSuccessful() = false
        override fun errorMessage() = error.message()
        override fun <T> map(mapper: Joke.Mapper<T>) = throw IllegalStateException()
    }
}