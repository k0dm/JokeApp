package com.example.jokeapp.presentation

import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ResultCallback


class ViewModel(private val repository: Repository) {

    private var dataCallback: DataCallback = DataCallback.Empty()
    private var resultCallback = object : ResultCallback {

        override fun provideJoke(jokeUI: JokeUi) {
            jokeUI.map(dataCallback)
        }
    }

    fun init(dataCallback: DataCallback) {
        this.dataCallback = dataCallback
        repository.init(resultCallback)
    }

    fun chooseFavorite(isChecked: Boolean) {
        repository.chooseFavorite(isChecked)
    }

    fun getJoke() {
        repository.getJoke()
    }

    fun clear() {
        dataCallback = DataCallback.Empty()
    }

    fun changeJokeStatus() {
        Thread {
            repository.changeJokeStatus(resultCallback)
        }.start()
    }
}