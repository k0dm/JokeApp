package com.example.jokeapp.presentation

interface ListProvider<E> {

    fun getList():List<CommonUi<E>>
}