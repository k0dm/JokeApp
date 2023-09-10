package com.example.jokeapp.data.cache

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class QuoteCache : RealmObject(), CommonItem<String> {

    @PrimaryKey
    var id: String = ""
    var author: String = ""
    var context: String = ""

    override fun <T> map(mapper: Mapper<String, T>): T {
        return mapper.map(id, author, context)
    }
}