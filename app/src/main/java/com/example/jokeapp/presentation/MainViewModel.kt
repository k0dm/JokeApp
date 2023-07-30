package com.example.jokeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokeapp.data.DispatcherList
import com.example.jokeapp.data.Joke
import com.example.jokeapp.data.Repository
import com.example.jokeapp.data.ToBaseUi
import com.example.jokeapp.data.ToFavoriteUi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val repository: Repository,
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi(),
    private val toFavoriteUi: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val dispatcherList: DispatcherList = DispatcherList.Base()
) : ViewModel() {

    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()
    fun init(jokeUiCallback: JokeUiCallback) {
        this.jokeUiCallback = jokeUiCallback
    }

    override fun onCleared() {
        super.onCleared()
        jokeUiCallback = JokeUiCallback.Empty()
    }

    fun chooseFavorite(isChecked: Boolean) {
        repository.chooseFavorite(isChecked)
    }

    fun getJoke() = viewModelScope.launch(dispatcherList.io()) {
        val result = repository.fetch()
        val jokeUi = if (result.isSuccessful()) {
            result.map(if (result.isFavorite()) toFavoriteUi else toBaseUi)
        } else {
            JokeUi.Failed(result.errorMessage())
        }
        withContext(dispatcherList.ui()) {
            jokeUi.show(jokeUiCallback)
        }
    }


    fun changeJokeStatus() = viewModelScope.launch(dispatcherList.io()) {
        val result = repository.changeJokeStatus()
        withContext(dispatcherList.ui()) {
            result.show(jokeUiCallback)
        }
    }
}
