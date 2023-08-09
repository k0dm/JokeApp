package com.example.jokeapp.data.cloud

import retrofit2.Call
import retrofit2.http.GET

interface QuoteService {

    @GET("https://api.quotable.io/random")
    fun fetch(): Call<QuoteCloud>
}