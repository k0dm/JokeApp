package com.example.jokeapp.domain

import com.example.jokeapp.core.Joke
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.data.FavoriteProvider
import com.example.jokeapp.data.JokeDataModel
import com.example.jokeapp.presentation.Error
import java.lang.IllegalStateException

interface JokeDomain : Joke, FavoriteProvider {

    fun isSuccessful(): Boolean
    fun errorMessage(): String

    class Success(private val joke: JokeDataModel) : JokeDomain {
        override fun isFavorite() =  joke.isFavorite()
        override fun isSuccessful() = true
        override fun errorMessage() = ""
        override fun <T> map(mapper: Mapper<T>) = joke.map(mapper)
    }

    class Failure(private val error: Error) : JokeDomain {
        override fun isFavorite() = false
        override fun isSuccessful() = false
        override fun errorMessage() = error.message()
        override fun <T> map(mapper: Mapper<T>) = throw IllegalStateException()
    }
}