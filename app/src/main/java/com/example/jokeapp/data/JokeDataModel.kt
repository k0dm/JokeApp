package com.example.jokeapp.data

import com.example.jokeapp.data.cache.ChangeJoke
import com.example.jokeapp.data.cache.ChangeJokeStatus
import com.example.jokeapp.core.Joke
import com.example.jokeapp.core.Mapper

class JokeDataModel(
    private val id: Int,
    private val text: String,
    private val punchline:String,
    private val type: String,
    private val isFavorite: Boolean = false
) : Joke, ChangeJoke, FavoriteProvider {
    override fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(id,text,punchline,type)
    }

    override suspend fun change(changeJokeStatus: ChangeJokeStatus): JokeDataModel {
        return changeJokeStatus.addOrRemove(id, this)
    }

    override fun isFavorite() = isFavorite
}