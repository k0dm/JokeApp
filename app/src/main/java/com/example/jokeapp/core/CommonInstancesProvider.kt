package com.example.jokeapp.core

import com.example.jokeapp.core.data.cache.PersistentDataSource
import com.example.jokeapp.core.data.cache.RealmProvider
import com.example.jokeapp.domain.FailureHandler

interface CommonInstancesProvider: RealmProvider {
    fun <T> makeService(service: Class<T>): T
    fun providePersistentDataSource(): PersistentDataSource
    fun provideFailureHandler(): FailureHandler
}