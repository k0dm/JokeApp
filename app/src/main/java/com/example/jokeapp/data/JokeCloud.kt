package com.example.jokeapp.data

import com.example.jokeapp.presentation.Joke
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
    fun toJoke() = Joke(text, punchLine)
}