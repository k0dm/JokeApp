package com.example.jokeapp.presentation.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

interface NavigationCommunication {

    fun observe(owner: LifecycleOwner, navigate: (Int) -> Unit)

    fun navigateTo(position: Int)

    class Base : NavigationCommunication {
        private val mutableLiveData = MutableLiveData<Int>()

        override fun observe(owner: LifecycleOwner, navigate: (Int) -> Unit) {
            mutableLiveData.observe(owner) { navigate.invoke(it) }
        }

        override fun navigateTo(position: Int) {
            mutableLiveData.value = position
        }
    }
}