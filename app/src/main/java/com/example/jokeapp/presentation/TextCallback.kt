package com.example.jokeapp.presentation

interface TextCallback {

    fun provideText(string: String)

    class Empty : TextCallback {

        override fun provideText(string: String) = Unit
    }
}