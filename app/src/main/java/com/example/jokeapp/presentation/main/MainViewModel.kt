package com.example.jokeapp.presentation.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val screenPosition: ScreenPosition,
    private val navigation: NavigationCommunication
) : ViewModel() {

    fun init() {
        navigateTo(screenPosition.load())
    }

    fun save(position: Int) {
        screenPosition.save(position)
        navigateTo(position)
    }

    fun observe(owner: LifecycleOwner, navigate: (Int) -> Unit) {
        navigation.observe(owner, navigate)
    }

    private fun navigateTo(position: Int) {
        navigation.navigateTo(position)
    }
}