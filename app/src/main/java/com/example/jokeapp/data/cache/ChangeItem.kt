package com.example.jokeapp.data.cache

import com.example.jokeapp.data.CommonDataModel
import java.lang.IllegalStateException

interface ChangeItem {

    suspend fun change(changeItemStatus: ChangeItemStatus) : CommonDataModel

    class Empty : ChangeItem {
        override suspend fun change(changeItemStatus: ChangeItemStatus): CommonDataModel {
            throw IllegalStateException("empty change item called")
        }
    }
}