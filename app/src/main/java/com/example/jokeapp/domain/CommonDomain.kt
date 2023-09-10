package com.example.jokeapp.domain

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.data.FavoriteProvider
import com.example.jokeapp.presentation.Error

interface CommonDomain<E> : CommonItem<E>, FavoriteProvider {

    fun isSuccessful(): Boolean
    fun errorMessage(): String

    class Success<E>(private val itemDataModel: CommonDataModel<E>) : CommonDomain<E> {
        override fun isFavorite() = itemDataModel.isFavorite()
        override fun isSuccessful() = true
        override fun errorMessage() = ""
        override fun <T> map(mapper: Mapper<E, T>) = itemDataModel.map(mapper)
    }

    class Failure<E>(private val error: Error) : CommonDomain<E> {
        override fun isFavorite() = false
        override fun isSuccessful() = false
        override fun errorMessage() = error.message()
        override fun <T> map(mapper: Mapper<E, T>) = throw IllegalStateException()
    }
}