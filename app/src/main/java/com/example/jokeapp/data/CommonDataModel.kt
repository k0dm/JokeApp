package com.example.jokeapp.data

import com.example.jokeapp.data.cache.ChangeItem
import com.example.jokeapp.data.cache.ChangeItemStatus
import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper


interface CommonDataModel<E> : CommonItem<E>, ChangeItem<E>, FavoriteProvider {
    class Base<E>(
        private val id: E,
        private val text: String,
        private val punchline: String,
        private val isFavorite: Boolean = false
    ) : CommonDataModel<E> {
        override fun <T> map(mapper: Mapper<E, T>): T {
            return mapper.map(id, text, punchline)
        }

        override suspend fun change(changeItemStatus: ChangeItemStatus<E>): CommonDataModel<E> {
            return changeItemStatus.addOrRemove(id, this)
        }

        override fun isFavorite() = isFavorite
    }
}
