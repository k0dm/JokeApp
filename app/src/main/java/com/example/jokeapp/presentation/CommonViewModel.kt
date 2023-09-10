package com.example.jokeapp.presentation

import android.util.Log
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface CommonViewModel<E> : ListChanges<E> {
    fun getItem()
    fun getItemList()
    fun changeItemStatus()
    fun changeItemStatus(id: E)
    fun chooseFavorites(favorites: Boolean)
    fun observe(owner: LifecycleOwner, observer: Observer<State>)
    fun observeList(owner: LifecycleOwner, observer: Observer<List<CommonUi<E>>>)

    abstract class Abstract<E>(
        name: String,
        private val interactor: CommonInteractor<E>,
        private val communication: StateCommunication<E>,
        private val progress: State = State.Progress(),
        private val toBaseUi: Mapper<E, CommonUi<E>> = ToBaseUi(),
        private val toFavoriteUi: Mapper<E, CommonUi<E>> = ToFavoriteUi(),
        dispatcherList: DispatcherList = DispatcherList.Base()
    ) : CommonViewModel<E>, BaseViewModel(name, dispatcherList) {

        private val blockUi: suspend (CommonDomain<E>) -> Unit = { jokeDomain ->
            val commonUi = if (jokeDomain.isSuccessful()) {
                jokeDomain.map(if (jokeDomain.isFavorite()) toFavoriteUi else toBaseUi)
            } else {
                CommonUi.Failed(jokeDomain.errorMessage())
            }
            commonUi.show(communication)

            getItemList()
        }

        override fun getItem() {
            communication.map(progress)
            handle({
                interactor.getItem()
            }, blockUi)
        }

        override fun getItemList() {
            handle({
                interactor.getItemList()
            }) { list ->
                communication.mapList(if (list[0].isSuccessful()) {
                    list.map {
                        it.map(toFavoriteUi)
                    }
                } else {
                    listOf(CommonUi.Failed(list[0].errorMessage()))
                })
            }
        }

        override fun getDiffResult() = communication.getDiffResult()

        override fun getList() = communication.getList()

        override fun changeItemStatus(id: E) {
            handle({
                interactor.removeItem(id)
                getItemList()
            }) {}
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

        override fun observeList(owner: LifecycleOwner, observer: Observer<List<CommonUi<E>>>) {
            communication.observeList(owner, observer)
        }
    }
}

abstract class BaseViewModel(
    private val name: String,
    private val dispatcherList: DispatcherList
) : ViewModel() {

    init {
        Log.d("k0dm", "init $name")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("k0dm", "onCleared $name")
    }

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