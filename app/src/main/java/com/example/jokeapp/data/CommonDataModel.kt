package com.example.jokeapp.data

import com.example.jokeapp.data.cache.ChangeItem
import com.example.jokeapp.data.cache.ChangeItemStatus
import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper


interface CommonDataModel : CommonItem, ChangeItem, FavoriteProvider {
    class Base(
        private val id: Int,
        private val text: String,
        private val punchline:String,
        private val isFavorite: Boolean = false
    ) : CommonDataModel {
        override fun <T> map(mapper: Mapper<T>): T {
            return mapper.map(id,text,punchline)
        }

        override suspend fun change(changeItemStatus: ChangeItemStatus): CommonDataModel {
            return changeItemStatus.addOrRemove(id, this)
        }

        override fun isFavorite() = isFavorite
    }
}
