package com.example.jokeapp.presentation

import androidx.lifecycle.ViewModel
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToFavoriteUi


class MainViewModel(
    private val repository: Repository,
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi(),
    private val toFavoriteUi: Joke.Mapper<JokeUi> = ToFavoriteUi()
) : ViewModel() {

    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()
    fun init(jokeUiCallback: JokeUiCallback) {
        this.jokeUiCallback = jokeUiCallback
    }

    fun chooseFavorite(isChecked: Boolean) {
        repository.chooseFavorite(isChecked)
    }

    fun getJoke() = Thread {
        val result = repository.fetch()
        if (result.isSuccessful()) {
            result.map(if (result.isFavorite()) toFavoriteUi else toBaseUi).show(jokeUiCallback)
        } else {
            JokeUi.Failed(result.errorMessage()).show(jokeUiCallback)
        }
    }.start()


    fun changeJokeStatus() = Thread {
        val result = repository.changeJokeStatus()
        result.show(jokeUiCallback)
    }.start()

}