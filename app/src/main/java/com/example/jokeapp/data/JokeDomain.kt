package com.example.jokeapp.data

import com.example.jokeapp.data.cache.ChangeJokeStatus
import com.example.jokeapp.data.cache.JokeCache
import com.example.jokeapp.presentation.JokeUi

interface Joke {

    fun <T> map(mapper: Joke.Mapper<T>): T

    interface Mapper<T> {
        fun map(
            id: Int,
            text: String,
            punchLine: String,
            type: String
        ): T
    }
}

data class JokeDomain(
    private val id: Int,
    private val text: String,
    private val punchLine: String,
    private val type: String
) : Joke {
    override fun <T> map(mapper: Joke.Mapper<T>): T {
        return mapper.map(id, text, punchLine, type)
    }

}

class ToDomain : Joke.Mapper<JokeDomain> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeDomain {
        return JokeDomain(id, text, punchLine, type)
    }
}

class ToCache : Joke.Mapper<JokeCache> {
    override fun map(id: Int, text: String, punchline: String, type: String): JokeCache {
        return JokeCache().apply {
            this.id = id
            this.text = text
            this.punchline = punchline
            this.type = type
        }
    }
}

class ToBaseUi : Joke.Mapper<JokeUi> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeUi {
        return JokeUi.Base(text, punchLine)
    }
}

class ToFavoriteUi : Joke.Mapper<JokeUi> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeUi {
        return JokeUi.Favorite(text, punchLine)
    }
}

class Change(private val cacheDataSource: ChangeJokeStatus) : Joke.Mapper<JokeResult> {
    override fun map(id: Int, text: String, punchLine: String, type: String): JokeResult {
        return cacheDataSource.addOrRemove(id, JokeDomain(id, text, punchLine, type))
    }
}