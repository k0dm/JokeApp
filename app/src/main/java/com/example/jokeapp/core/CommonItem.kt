package com.example.jokeapp.core

interface CommonItem {

    fun <T> map(mapper: Mapper<T>): T
}

