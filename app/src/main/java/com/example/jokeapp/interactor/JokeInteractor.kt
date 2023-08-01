package com.example.jokeapp.interactor

import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToFavoriteUi
import com.example.jokeapp.presentation.JokeUi

interface JokeInteractor {

    suspend fun getJoke(): JokeUi

    suspend fun changeJokeStatus(): JokeUi

    fun getFavoriteJokes(favorites: Boolean)

    class Base(
        private val repository: Repository,
        private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi(),
        private val toFavoriteUi: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    ) : JokeInteractor {

        override suspend fun getJoke(): JokeUi {
            val result = repository.fetch()
            return if (result.isSuccessful()) {
                result.map(if (result.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                JokeUi.Failed(result.errorMessage())
            }
        }

        override suspend fun changeJokeStatus(): JokeUi {
            val result = repository.changeJokeStatus()
            return if (result.isSuccessful()) {
                result.map(if (result.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                JokeUi.Failed(result.errorMessage())
            }
        }

        override fun getFavoriteJokes(favorites: Boolean) {
            repository.chooseFavorite(favorites)
        }
    }
}