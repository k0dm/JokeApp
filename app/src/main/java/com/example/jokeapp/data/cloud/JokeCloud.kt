package com.example.jokeapp.data.cloud

import com.example.jokeapp.core.Joke
import com.example.jokeapp.core.Mapper
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
) : Joke {

    override fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(id, text, punchLine, type)
    }
}