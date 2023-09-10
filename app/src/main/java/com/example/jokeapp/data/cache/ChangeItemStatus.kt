package com.example.jokeapp.data.cache

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.data.CommonDataModel

interface ChangeItemStatus<E> {
    fun addOrRemove(id: E, commonItemDataModel: CommonItem<E>): CommonDataModel<E>
}