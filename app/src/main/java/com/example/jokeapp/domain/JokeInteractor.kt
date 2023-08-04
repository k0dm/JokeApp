package com.example.jokeapp.domain

import com.example.jokeapp.data.Repository

import java.lang.Exception

interface JokeInteractor {

    suspend fun getJoke(): JokeDomain

    suspend fun changeJokeStatus(): JokeDomain

    fun getFavoriteJokes(favorites: Boolean)

    class Base(
        private val repository: Repository,
        private val jokeFailureHandler: JokeFailureHandler
    ) : JokeInteractor {


        override suspend fun getJoke(): JokeDomain {
            return  try {
                val resultJoke = repository.fetch()
                 JokeDomain.Success(resultJoke)
            }catch (e: Exception){
                 JokeDomain.Failure(jokeFailureHandler.handle(e))
            }
        }

        override suspend fun changeJokeStatus(): JokeDomain {
            return try {
                val resultJoke = repository.changeJokeStatus()
                return JokeDomain.Success(resultJoke)
            }catch (e: Exception){
                JokeDomain.Failure(jokeFailureHandler.handle(e))
            }

        }

        override fun getFavoriteJokes(favorites: Boolean) {
            repository.chooseFavorite(favorites)
        }
    }
}