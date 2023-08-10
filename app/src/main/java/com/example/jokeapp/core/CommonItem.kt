package com.example.jokeapp.core

interface CommonItem<E> {

    fun <T>map(mapper: Mapper<E, T>): T
}

