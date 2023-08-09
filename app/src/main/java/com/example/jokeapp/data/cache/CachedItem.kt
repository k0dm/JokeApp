package com.example.jokeapp.data.cache

import com.example.jokeapp.data.CommonDataModel

interface CachedItem : ChangeItem {

    fun save(itemData: CommonDataModel)
    fun clear()

    class Base : CachedItem {
        private var item: ChangeItem = ChangeItem.Empty()

        override fun save(itemData: CommonDataModel) {
            item = itemData
        }

        override fun clear() {
            item = ChangeItem.Empty()
        }

        override suspend fun change(changeItemStatus: ChangeItemStatus): CommonDataModel {
            return item.change(changeItemStatus)
        }
    }
}