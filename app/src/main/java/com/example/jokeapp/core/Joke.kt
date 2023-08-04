package com.example.jokeapp.core

interface Joke {

    fun <T> map(mapper: Mapper<T>): T
}

