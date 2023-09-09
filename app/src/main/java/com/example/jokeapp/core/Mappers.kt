package com.example.jokeapp.core

import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.data.cache.JokeCache
import com.example.jokeapp.data.cache.QuoteCache
import com.example.jokeapp.presentation.CommonUi

interface Mapper<E, T> {
    fun map(
        id: E,
        firstText: String,
        secondText: String,
    ): T
}

abstract class DataIsFavorite<E>(private val isFavorite: Boolean) : Mapper<E, CommonDataModel<E>> {
    override fun map(id: E, firstText: String, secondText: String): CommonDataModel<E> {
        return CommonDataModel.Base(id, firstText, secondText, isFavorite)
    }
}

class ToDataIsNotFavorite<E> : DataIsFavorite<E>(false)

class ToDataIsFavorite<E> : DataIsFavorite<E>(true)

class ToCacheJoke : Mapper<Int, JokeCache> {
    override fun map(id: Int, firstText: String, secondText: String): JokeCache {
        return JokeCache().apply {
            this.id = id
            this.text = firstText
            this.punchline = secondText
        }
    }
}

class ToCacheQuote : Mapper<String, QuoteCache> {
    override fun map(id: String, firstText: String, secondText: String): QuoteCache {
        return QuoteCache().apply {
            this.id = id
            this.author = firstText
            this.context = secondText
        }
    }
}

class ToBaseUi<E> : Mapper<E, CommonUi<E>> {
    override fun map(id: E, firstText: String, secondText: String): CommonUi<E> {
        return CommonUi.Base<E>(id, firstText, secondText)
    }
}

class ToFavoriteUi<E> : Mapper<E, CommonUi<E>> {
    override fun map(id: E, firstText: String, secondText: String): CommonUi<E> {
        return CommonUi.Favorite<E>(id, firstText, secondText)
    }
}