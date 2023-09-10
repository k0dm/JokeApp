package com.example.jokeapp.core.data.cache

import io.realm.Realm

interface RealmProvider {

    fun provideRealm(): Realm
}