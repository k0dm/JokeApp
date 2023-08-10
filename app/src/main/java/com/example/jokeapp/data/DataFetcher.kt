package com.example.jokeapp.data

interface DataFetcher<E> {

    suspend fun fetch(): CommonDataModel<E>
}

//interface ItemDataFetcher<E>: DataFetcher<CommonDataModel<E>>