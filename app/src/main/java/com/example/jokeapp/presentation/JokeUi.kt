package com.example.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.example.jokeapp.R

abstract class JokeUi(
    private val text: String,
    private val punchline: String,
    @DrawableRes
    private val iconResId: Int
) {

    fun map(dataCallback: DataCallback) {
        dataCallback.provideText("$text\n$punchline")
        dataCallback.provideResId(iconResId)
    }

    class Base(text: String, punchline: String) :
        JokeUi(text, punchline, R.drawable.favorite_border_36)

    class Favorite(text: String, punchline: String) :
        JokeUi(text, punchline, R.drawable.favorite_36)

    class Failed(text: String) : JokeUi(text, "", 0)
}