package com.example.jokeapp.data.cloud


import com.example.jokeapp.data.JokeCloud
import retrofit2.Call
import retrofit2.http.GET
import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

interface JokeService {

    @GET("random_joke")
    fun getJoke(): Call<JokeCloud>
}