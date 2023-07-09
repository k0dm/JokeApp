package com.example.jokeapp.presentation

import com.example.jokeapp.data.Model
import com.example.jokeapp.data.ResultCallback


class ViewModel(private val repository: Model<Joke, Error>) {

    private var callback: TextCallback = TextCallback.Empty()

    fun init(textCallback: TextCallback) {
        this.callback = textCallback
        repository.init(object : ResultCallback<Joke, Error> {

            override fun provideSuccess(data: Joke) = textCallback.provideText(data.getJokeUi())

            override fun provideError(error: Error) = textCallback.provideText(error.message())
        })
    }

    fun getJoke() {
        repository.getJoke()
    }

    fun clear() {
        callback = TextCallback.Empty()
    }
}