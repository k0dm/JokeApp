package com.example.jokeapp.core

import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.data.cache.JokeCache
import com.example.jokeapp.data.cache.QuoteCache
import com.example.jokeapp.presentation.CommonUi

interface Mapper<T> {
    fun map(
        id: Int,
        firstText: String,
        secondText: String,
    ): T

}

abstract class DataIsFavorite(private val isFavorite: Boolean) : Mapper<CommonDataModel> {
    override fun map(id: Int, firstText: String, secondText: String): CommonDataModel {
        return CommonDataModel.Base(id, firstText, secondText,isFavorite)
    }
}

class ToDataIsNotFavorite: DataIsFavorite(false)

class ToDataIsFavorite: DataIsFavorite(true)

class ToCacheJoke : Mapper<JokeCache> {
    override fun map(id: Int, firstText: String, secondText: String): JokeCache {
        return JokeCache().apply {
            this.id = id
            this.text = firstText
            this.punchline = secondText
        }
    }
}

class ToCacheQuote : Mapper<QuoteCache> {
    override fun map(id: Int, firstText: String, secondText: String): QuoteCache {
        return QuoteCache().apply {
            this.id = id
            this.author = firstText
            this.context = secondText
        }
    }
}

class ToBaseUi : Mapper<CommonUi> {
    override fun map(id: Int, firstText: String, secondText: String): CommonUi {
        return CommonUi.Base(firstText, secondText)
    }
}

class ToFavoriteUi : Mapper<CommonUi> {
    override fun map(id: Int, firstText: String, secondText: String): CommonUi {
        return CommonUi.Favorite(firstText, secondText)
    }
}