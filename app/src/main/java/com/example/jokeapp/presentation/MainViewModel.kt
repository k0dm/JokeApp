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
    dispatcherList: DispatcherList = DispatcherList.Base()
) : BaseViewModel(dispatcherList) {

    private var jokeUiCallback: JokeUiCallback = JokeUiCallback.Empty()
    private val blockUi: suspend (JokeUi) -> Unit = {
        it.show(jokeUiCallback)
    }

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

    fun getJoke() {
        handle({
            val result = repository.fetch()
            if (result.isSuccessful()) {
                result.map(if (result.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                JokeUi.Failed(result.errorMessage())
            }
        }, blockUi)
    }

    fun changeJokeStatus() {
        handle({ repository.changeJokeStatus() }, blockUi)
    }
}


abstract class BaseViewModel(
    private val dispatcherList: DispatcherList
) : ViewModel() {
    fun <T> handle(
        blockIo: suspend () -> T,
        blockUi: suspend (T) -> Unit
    ) {
        viewModelScope.launch(dispatcherList.io()) {
            val jokeUi = blockIo.invoke()
            withContext(dispatcherList.ui()) {
                blockUi.invoke(jokeUi)
            }
        }
    }
}
