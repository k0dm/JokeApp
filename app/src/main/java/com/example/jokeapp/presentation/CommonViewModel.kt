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
import com.example.jokeapp.domain.StateCommunication
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface CommonViewModel<E> {
    fun getItem()
    fun changeItemStatus()
    fun chooseFavorites(favorites: Boolean)
    fun observe(owner: LifecycleOwner, observer: Observer<State>)

    class Joke(
        private val interactor: CommonInteractor<Int>,
        private val communication: StateCommunication = StateCommunication.Base(),
        private val progress: State = State.Progress(),
        private val toBaseUi: Mapper<Int,CommonUi> = ToBaseUi(),
        private val toFavoriteUi: Mapper<Int,CommonUi> = ToFavoriteUi(),
        dispatcherList: DispatcherList = DispatcherList.Base()
    ) : CommonViewModel<Int>, BaseViewModel(dispatcherList) {

        private val blockUi: suspend (CommonDomain<Int>) -> Unit = { jokeDomain ->
            val commonUi = if (jokeDomain.isSuccessful()) {
                jokeDomain.map(if (jokeDomain.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                CommonUi.Failed(jokeDomain.errorMessage())
            }
            commonUi.show(communication)
        }

        override fun getItem() {
            communication.map(progress)
            handle({
                interactor.getItem()
            }, blockUi)
        }

        override fun changeItemStatus() {
            handle({
                interactor.changeItemStatus()
            }, blockUi)
        }

        override fun chooseFavorites(favorites: Boolean) {
            interactor.chooseFavorite(favorites)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
            communication.observe(owner, observer)
        }

    }

    class Quote(
        private val interactor: CommonInteractor<String>,
        private val communication: StateCommunication = StateCommunication.Base(),
        private val progress: State = State.Progress(),
        private val toBaseUi: Mapper<String,CommonUi> = ToBaseUi(),
        private val toFavoriteUi: Mapper<String,CommonUi> = ToFavoriteUi(),
        dispatcherList: DispatcherList = DispatcherList.Base()
    ) : CommonViewModel<String>, BaseViewModel(dispatcherList) {

        private val blockUi: suspend (CommonDomain<String>) -> Unit = { jokeDomain ->
            val commonUi = if (jokeDomain.isSuccessful()) {
                jokeDomain.map(if (jokeDomain.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                CommonUi.Failed(jokeDomain.errorMessage())
            }
            commonUi.show(communication)
        }

        override fun getItem() {
            communication.map(progress)
            handle({
                interactor.getItem()
            }, blockUi)
        }

        override fun changeItemStatus() {
            handle({
                interactor.changeItemStatus()
            }, blockUi)
        }

        override fun chooseFavorites(favorites: Boolean) {
            interactor.chooseFavorite(favorites)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
            communication.observe(owner, observer)
        }

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