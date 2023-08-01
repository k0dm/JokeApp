package com.example.jokeapp.data.cloud

import com.example.jokeapp.data.Joke
import com.google.gson.annotations.SerializedName

data class NewJokeCloud(
    @SerializedName("id")
    private val id: Int,
    @SerializedName("setup")
    private val text: String,
    @SerializedName("delivery")
    private val punchline: String,
    @SerializedName("joke")
    private val joke: String,
    @SerializedName("type")
    private val type: String
) : Joke {

    @JvmOverloads
    constructor(
        id: Int,
        joke: String,
        type: String
    ) : this(id, joke, "", joke, type)

    // Additional constructor to handle the "twopart" type joke
    @JvmOverloads
    constructor(
        id: Int,
        setup: String,
        delivery: String,
        type: String,

        ) : this(id, setup, delivery, "", type)

    override fun <T> map(mapper: Joke.Mapper<T>): T {
        return mapper.map(id, text, punchline, type)
    }
}