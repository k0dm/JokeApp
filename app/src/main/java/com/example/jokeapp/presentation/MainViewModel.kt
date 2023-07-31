package com.example.jokeapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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
    private val communication: Communication<State>,
    private val repository: Repository,
    private val toBaseUi: Joke.Mapper<JokeUi> = ToBaseUi(),
    private val toFavoriteUi: Joke.Mapper<JokeUi> = ToFavoriteUi(),
    private val progress: State,
    dispatcherList: DispatcherList = DispatcherList.Base()
) : BaseViewModel(dispatcherList), Observe<State>{

    private val blockUi: suspend (JokeUi) -> Unit = {
        it.show(communication)
    }
    override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
        communication.observe(owner, observer)
    }
    fun chooseFavorite(isChecked: Boolean) {
        repository.chooseFavorite(isChecked)
    }

    fun getJoke() {
        communication.map(progress)
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
