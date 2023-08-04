package com.example.jokeapp.views

interface Show<T> {
    fun show(arg: T)
}

interface ShowButton: Show<Boolean>
interface ShowImageView: Show<Int>
interface ShowProgressBar: Show<Boolean>
interface ShowText: Show<String>
