package com.example.jokeapp.data

import com.example.jokeapp.data.cache.CacheDataSource
import com.example.jokeapp.presentation.JokeUi
import com.google.gson.annotations.SerializedName

data class JokeCloud(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("setup")
    private val text: String,
    @SerializedName("punchline")
    private val punchLine: String,
    @SerializedName("type")
    private val type: String
) {
    fun toBaseJoke() = JokeUi.Base(text, punchLine)
    fun toFavoriteJoke() = JokeUi.Favorite(text, punchLine)
    fun change(cacheDataSource: CacheDataSource) = cacheDataSource.addOrRemove(id, this)

}