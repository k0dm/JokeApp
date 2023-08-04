package com.example.jokeapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokeapp.core.DispatcherList
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.core.ToBaseUi
import com.example.jokeapp.core.ToFavoriteUi
import com.example.jokeapp.domain.Communication
import com.example.jokeapp.domain.JokeDomain
import com.example.jokeapp.domain.JokeInteractor
import com.example.jokeapp.domain.Observe
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val interactor: JokeInteractor,
    private val communication: Communication<State>,
    private val progress: State,
    private val toBaseUi: Mapper<JokeUi> = ToBaseUi(),
    private val toFavoriteUi: Mapper<JokeUi> = ToFavoriteUi(),
    dispatcherList: DispatcherList = DispatcherList.Base()
) : BaseViewModel(dispatcherList), Observe<State> {

    private val blockUi: suspend (JokeDomain) -> Unit = { jokeDomain ->
        val jokeUi = if (jokeDomain.isSuccessful()) {
                jokeDomain.map(
                    if (jokeDomain.isFavorite()) {
                        toFavoriteUi
                    } else toBaseUi
                )
            } else {
                JokeUi.Failed(jokeDomain.errorMessage())
            }
        jokeUi.show(communication)
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
