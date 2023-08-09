package com.example.jokeapp.domain

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.data.FavoriteProvider
import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.presentation.Error
import java.lang.IllegalStateException

interface CommonDomain : CommonItem, FavoriteProvider {

    fun isSuccessful(): Boolean
    fun errorMessage(): String

    class Success(private val itemDataModel: CommonDataModel) : CommonDomain {
        override fun isFavorite() =  itemDataModel.isFavorite()
        override fun isSuccessful() = true
        override fun errorMessage() = ""
        override fun <T> map(mapper: Mapper<T>) = itemDataModel.map(mapper)
    }

    class Failure(private val error: Error) : CommonDomain {
        override fun isFavorite() = false
        override fun isSuccessful() = false
        override fun errorMessage() = error.message()
        override fun <T> map(mapper: Mapper<T>) = throw IllegalStateException()
    }
}