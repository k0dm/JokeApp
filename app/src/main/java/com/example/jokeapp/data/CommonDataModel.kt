package com.example.jokeapp.data

import com.example.jokeapp.data.cache.ChangeItem
import com.example.jokeapp.data.cache.ChangeItemStatus
import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.core.ShowText


interface CommonDataModel<E> : CommonItem<E>, ChangeItem<E>, FavoriteProvider {
    fun showText(showText: ShowText)

    class Base<E>(
        private val id: E,
        private val firstText: String,
        private val secondText: String,
        private val isFavorite: Boolean = false
    ) : CommonDataModel<E> {
        override fun <T> map(mapper: Mapper<E, T>): T {
            return mapper.map(id, firstText, secondText)
        }

        override suspend fun change(changeItemStatus: ChangeItemStatus<E>): CommonDataModel<E> {
            return changeItemStatus.addOrRemove(id, this)
        }

        override fun isFavorite() = isFavorite

        override fun showText(showText: ShowText) = showText.show(firstText)
    }
}
