package com.example.jokeapp.data.cache

import com.example.jokeapp.data.CommonDataModel
import java.lang.IllegalStateException

interface ChangeItem<E> {

    suspend fun change(changeItemStatus: ChangeItemStatus<E>) : CommonDataModel<E>

    class Empty<E> : ChangeItem<E> {
        override suspend fun change(changeItemStatus: ChangeItemStatus<E>): CommonDataModel<E> {
            throw IllegalStateException("empty change item called")
        }
    }
}