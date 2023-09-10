package com.example.jokeapp.presentation.main

import com.example.jokeapp.core.data.cache.PersistentDataSource

interface ScreenPosition {
    fun save(position: Int)
    fun load(): Int

    class Base(private val persistentDataSource: PersistentDataSource) : ScreenPosition {

        override fun save(position: Int) {
            persistentDataSource.save(position, SCREEN_POSITION, KEY)
        }

        override fun load() = persistentDataSource.load(SCREEN_POSITION, KEY)

        private companion object {
            const val SCREEN_POSITION = "screenPosition"
            const val KEY = "screenPositionKey"
        }
    }
}