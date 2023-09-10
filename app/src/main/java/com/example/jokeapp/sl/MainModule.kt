package com.example.jokeapp.sl

import com.example.jokeapp.core.data.cache.PersistentDataSource
import com.example.jokeapp.presentation.main.MainViewModel
import com.example.jokeapp.presentation.main.NavigationCommunication
import com.example.jokeapp.presentation.main.ScreenPosition

class MainModule(private val persistentDataSource: PersistentDataSource): Module<MainViewModel> {
    override fun getViewModel(): MainViewModel = MainViewModel(
        ScreenPosition.Base(persistentDataSource),
        NavigationCommunication.Base()
    )
}