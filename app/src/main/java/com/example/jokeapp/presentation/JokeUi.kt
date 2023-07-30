package com.example.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.example.jokeapp.R


interface JokeUi {

    fun show(jokeUiCallback: JokeUiCallback)
    abstract class Abstract(
        private val text: String,
        private val punchline: String,
        @DrawableRes
        private val iconResId: Int
    ) : JokeUi {

        override fun show(jokeUiCallback: JokeUiCallback) = with(jokeUiCallback) {
            provideText("$text\n$punchline")
            provideResId(iconResId)
        }
    }

    class Base(text: String, punchline: String) :
        Abstract(text, punchline, R.drawable.favorite_border_36)

    class Favorite(text: String, punchline: String) :
        Abstract(text, punchline, R.drawable.favorite_36)

    class Failed(text: String) : Abstract(text, "", 0)

}