package com.example.jokeapp.data.cache

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import com.example.jokeapp.data.CommonDataModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class JokeCache : RealmObject(), CommonItem<Int> {
    @PrimaryKey
    var id: Int = -1
    var text: String = ""
    var punchline: String = ""

    override fun <T> map(mapper: Mapper<Int, T>): T {
            return mapper.map(id, text, punchline)
    }
}