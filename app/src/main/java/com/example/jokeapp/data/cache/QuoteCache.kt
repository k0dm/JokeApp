package com.example.jokeapp.data.cache

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class QuoteCache: RealmObject(), CommonItem  {

    @PrimaryKey
    var id: Int = -1
    var author: String = ""
    var context: String = ""

    override fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(id, author, context)
    }
}