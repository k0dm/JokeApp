package com.example.jokeapp.domain

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.jokeapp.presentation.State

interface Communication<T : Any> : Observe<T> {

    fun map(data: T)

    abstract class Abstract<T : Any>(private val liveData: MutableLiveData<T> = MutableLiveData()) :
        Communication<T> {
        override fun map(data: T) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }
}

interface StateCommunication: Communication<State> {
    class Base : Communication.Abstract<State>(), StateCommunication
}

interface Observe<T : Any> {
    fun observe(owner: LifecycleOwner, observer: Observer<T>) = Unit
}
