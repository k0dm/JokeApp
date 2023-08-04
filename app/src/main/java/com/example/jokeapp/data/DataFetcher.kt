package com.example.jokeapp.data

interface DataFetcher<T> {

    suspend fun fetch(): T
}

interface JokeDataFetcher: DataFetcher<JokeDataModel>