package com.example.jokeapp.data.cache

import com.example.jokeapp.data.CommonDataModel

interface CachedItem<E> : ChangeItem<E> {

    fun save(itemData: CommonDataModel<E>)
    fun clear()

    class Base<E> : CachedItem<E> {
        private var item: ChangeItem<E> = ChangeItem.Empty()

        override fun save(itemData: CommonDataModel<E>) {
            item = itemData
        }

        override fun clear() {
            item = ChangeItem.Empty()
        }

        override suspend fun change(changeItemStatus: ChangeItemStatus<E>): CommonDataModel<E> {
            return item.change(changeItemStatus)
        }
    }
}