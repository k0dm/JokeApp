package com.example.jokeapp.data.cache

import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.core.CommonItem

interface ChangeItemStatus<E> {
    fun addOrRemove(id: E, commonItemDataModel: CommonItem<E>): CommonDataModel<E>
}