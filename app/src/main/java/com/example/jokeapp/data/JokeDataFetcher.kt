package com.example.jokeapp.data

interface JokeDataFetcher<T> {

    suspend fun fetch(): T
}