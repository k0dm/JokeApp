package com.example.jokeapp.core

import com.example.jokeapp.data.JokeDataModel
import com.example.jokeapp.data.FavoriteProvider
import com.example.jokeapp.data.cache.ChangeJokeStatus
import com.example.jokeapp.data.cache.JokeCache
import com.example.jokeapp.domain.JokeDomain
import com.example.jokeapp.presentation.JokeUi

interface Mapper<T> {
    fun map(
        id: Int,
        text: String,
        punchLine: String,
        type: String
    ): T

}

abstract class DataIsFavorite(private val isFavorite: Boolean) : Mapper<JokeDataModel> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeDataModel {
        return JokeDataModel.Base(id, text, punchLine, type,isFavorite)
    }
}

class ToDataIsNotFavorite: DataIsFavorite(false)

class ToDataIsFavorite: DataIsFavorite(true)

class ToCache : Mapper<JokeCache> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeCache {
        return JokeCache().apply {
            this.id = id
            this.text = text
            this.punchline = punchLine
            this.type = type
        }
    }
}

class ToBaseUi : Mapper<JokeUi> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeUi {
        return JokeUi.Base(text, punchLine)
    }
}

class ToFavoriteUi : Mapper<JokeUi> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeUi {
        return JokeUi.Favorite(text, punchLine)
    }
}

class Change(private val cacheDataSource: ChangeJokeStatus) : Mapper<JokeDataModel> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeDataModel {
        return cacheDataSource.addOrRemove(id, JokeDataModel.Base(id, text, punchLine, type))
    }
}