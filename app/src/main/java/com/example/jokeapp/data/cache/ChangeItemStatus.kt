package com.example.jokeapp.data.cache

import com.example.jokeapp.data.CommonDataModel
import com.example.jokeapp.core.CommonItem

interface ChangeItemStatus {
    fun addOrRemove(id: Int, commonItemDataModel: CommonItem): CommonDataModel
}