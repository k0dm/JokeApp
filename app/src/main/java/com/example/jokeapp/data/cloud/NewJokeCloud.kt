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

    override fun <T> map(mapper: Joke.Mapper<T>): T {

        return if (type == "twopart"){
             mapper.map(id, text, punchline, type)
        }else {
            mapper.map(id, joke, "", type)
        }
    }
}