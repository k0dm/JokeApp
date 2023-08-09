package com.example.jokeapp.domain

import android.util.Log
import com.example.jokeapp.data.Repository

import java.lang.Exception

interface CommonInteractor {

    suspend fun getItem(): CommonDomain

    suspend fun changeItemStatus(): CommonDomain

    fun chooseFavorite(favorites: Boolean)

    class Base(
        private val repository: Repository,
        private val failureHandler: FailureHandler
    ) : CommonInteractor {

        override suspend fun getItem(): CommonDomain {
            return  try {
                val resultItem = repository.fetch()
                 CommonDomain.Success(resultItem)
            }catch (e: Exception){
                 CommonDomain.Failure(failureHandler.handle(e))
            }
        }

        override suspend fun changeItemStatus(): CommonDomain {
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