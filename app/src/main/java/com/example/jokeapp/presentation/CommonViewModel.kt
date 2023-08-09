package com.example.jokeapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.jokeapp.core.DispatcherList
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.core.ToBaseUi
import com.example.jokeapp.core.ToFavoriteUi
import com.example.jokeapp.domain.CommonDomain
import com.example.jokeapp.domain.CommonInteractor
import com.example.jokeapp.domain.StateCommunication

interface CommonViewModel {
    fun getItem()
    fun changeItemStatus()
    fun chooseFavorites(favorites: Boolean)
    fun observe(owner: LifecycleOwner, observer: Observer<State>)

    class Joke(
        private val interactor: CommonInteractor,
        private val communication: StateCommunication = StateCommunication.Base(),
        private val progress: State = State.Progress(),
        private val toBaseUi: Mapper<CommonUi> = ToBaseUi(),
        private val toFavoriteUi: Mapper<CommonUi> = ToFavoriteUi(),
        dispatcherList: DispatcherList = DispatcherList.Base()
    ) : CommonViewModel, BaseViewModel(dispatcherList) {

        private val blockUi: suspend (CommonDomain) -> Unit = { jokeDomain ->
            val commonUi = if (jokeDomain.isSuccessful()) {
                jokeDomain.map(if (jokeDomain.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                CommonUi.Failed(jokeDomain.errorMessage())
            }
            commonUi.show(communication)
        }

        override fun getItem() {
            communication.map(State.Progress())
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
        private val interactor: CommonInteractor,
        private val communication: StateCommunication = StateCommunication.Base(),
        private val progress: State = State.Progress(),
        private val toBaseUi: Mapper<CommonUi> = ToBaseUi(),
        private val toFavoriteUi: Mapper<CommonUi> = ToFavoriteUi(),
        dispatcherList: DispatcherList = DispatcherList.Base()
    ) : CommonViewModel, BaseViewModel(dispatcherList) {

        private val blockUi: suspend (CommonDomain) -> Unit = { jokeDomain ->
            val commonUi = if (jokeDomain.isSuccessful()) {
                jokeDomain.map(if (jokeDomain.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                CommonUi.Failed(jokeDomain.errorMessage())
            }
            commonUi.show(communication)
        }

        override fun getItem() {
            communication.map(State.Progress())
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