package com.example.jokeapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication<T : Any, E> : ListCommunication<E>, ListProvider<E> {

    fun map(data: T)

    fun observe(owner: LifecycleOwner, observer: Observer<T>)

    abstract class Abstract<T : Any, E>(
        private val liveData: MutableLiveData<T> = MutableLiveData(),
        private val listLiveData: MutableLiveData<ArrayList<CommonUi<E>>> = MutableLiveData()
    ) : Communication<T, E> {
        override fun map(data: T) {
            liveData.value = data
        }

        override fun mapList(list: List<CommonUi<E>>) {
            listLiveData.value = ArrayList(list)
        }

        override fun removeItem(id: E): Int {
            val item = listLiveData.value?.find { it.matches(id) }
            val position = listLiveData.value?.indexOf(item) ?: -1
            listLiveData.value?.remove(item)
            return position
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }

        override fun observeList(owner: LifecycleOwner, observer: Observer<List<CommonUi<E>>>) {
            listLiveData.observe(owner, observer)
        }

        override fun getList(): List<CommonUi<E>> = listLiveData.value ?: emptyList()
    }
}

interface StateCommunication<E> : Communication<State, E> {
    class Base<E> : Communication.Abstract<State, E>(), StateCommunication<E>
}


interface ListCommunication<E> {

    fun mapList(list: List<CommonUi<E>>)
    fun observeList(owner: LifecycleOwner, observer: Observer<List<CommonUi<E>>>)
    fun removeItem(id: E): Int
}