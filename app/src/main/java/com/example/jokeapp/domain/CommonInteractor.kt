package com.example.jokeapp.domain

import android.util.Log
import com.example.jokeapp.data.Repository

import java.lang.Exception

interface CommonInteractor<E> {

    suspend fun getItem(): CommonDomain<E>

    suspend fun changeItemStatus(): CommonDomain<E>

    fun chooseFavorite(favorites: Boolean)

    class Base<E>(
        private val repository: Repository<E>,
        private val failureHandler: FailureHandler
    ) : CommonInteractor<E> {

        override suspend fun getItem(): CommonDomain<E> {
            return  try {
                val resultItem = repository.fetch()
                 CommonDomain.Success(resultItem)
            }catch (e: Exception){
                 CommonDomain.Failure(failureHandler.handle(e))
            }
        }

        override suspend fun changeItemStatus(): CommonDomain<E> {
            return try {
                val resultJoke = repository.changeJokeStatus()
                return CommonDomain.Success(resultJoke)
            }catch (e: Exception){
                Log.d("k0dm", e.toString())
                CommonDomain.Failure(failureHandler.handle(e))
            }

        }

        override fun chooseFavorite(favorites: Boolean) {
            repository.chooseFavorite(favorites)
        }
    }
}