package com.example.jokeapp.data.cloud

import com.example.jokeapp.core.CommonItem
import com.example.jokeapp.core.Mapper
import com.google.gson.annotations.SerializedName

class QuoteCloud(
    @SerializedName("_id")
    private val id: String,
    @SerializedName("author")
    private val author: String,
    @SerializedName("content")
    private val content: String
) : CommonItem {
    override fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(System.currentTimeMillis().toInt(), author, content)
    }
}