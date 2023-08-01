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
import com.example.jokeapp.interactor.JokeInteractor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val interactor: JokeInteractor,
    private val communication: Communication<State>,
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
        interactor.getFavoriteJokes(isChecked)
    }

    fun getJoke() {
        communication.map(progress)
        handle({
            interactor.getJoke()
        }, blockUi)
    }

    fun changeJokeStatus() {
        handle({ interactor.changeJokeStatus() }, blockUi)
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
