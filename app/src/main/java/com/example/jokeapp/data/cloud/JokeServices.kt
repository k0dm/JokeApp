package com.example.jokeapp.data.cloud


import retrofit2.Call
import retrofit2.http.GET

interface BaseJokeService {
    @GET("https://official-joke-api.appspot.com/random_joke")
    fun fetch(): Call<JokeCloud>
}

interface NewJokeService {
    @GET("https://v2.jokeapi.dev/joke/Any")
    fun fetch(): Call<NewJokeCloud>
}