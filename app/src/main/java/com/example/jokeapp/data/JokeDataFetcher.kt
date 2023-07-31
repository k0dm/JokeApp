package com.example.jokeapp.data

interface JokeDataFetcher<T> {

    fun fetch(): T
}