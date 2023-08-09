package com.example.jokeapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokeapp.core.DispatcherList
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.core.ToBaseUi
import com.example.jokeapp.core.ToFavoriteUi
import com.example.jokeapp.domain.CommonDomain
import com.example.jokeapp.domain.CommonInteractor
import com.example.jokeapp.domain.Observe
import com.example.jokeapp.domain.StateCommunication
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val interactor: CommonInteractor,
    private val communication: StateCommunication,
    private val progress: State,
    private val toBaseUi: Mapper<CommonUi> = ToBaseUi(),
    private val toFavoriteUi: Mapper<CommonUi> = ToFavoriteUi(),
    dispatcherList: DispatcherList = DispatcherList.Base()
) : BaseViewModel(dispatcherList), Observe<State> {

    private val blockUi: suspend (CommonDomain) -> Unit = { jokeDomain ->
        val commonUi = if (jokeDomain.isSuccessful()) {
            jokeDomain.map(
                if (jokeDomain.isFavorite()) {
                    toFavoriteUi
                } else toBaseUi
            )
        } else {
            CommonUi.Failed(jokeDomain.errorMessage())
        }
        commonUi.show(communication)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
        communication.observe(owner, observer)
    }

    fun chooseFavorite(isChecked: Boolean) {
        interactor.chooseFavorite(isChecked)
    }

    fun getJoke() {
        communication.map(progress)
        handle({
            interactor.getItem()
        }, blockUi)
    }

    fun changeJokeStatus() {
        handle({ interactor.changeItemStatus() }, blockUi)
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
