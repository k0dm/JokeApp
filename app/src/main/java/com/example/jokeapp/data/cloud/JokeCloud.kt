package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.JokeDomain
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

    override fun <T> map(mapper: Joke.Mapper<T>): T {
        return mapper.map(id, text, punchLine, type)
    }
}