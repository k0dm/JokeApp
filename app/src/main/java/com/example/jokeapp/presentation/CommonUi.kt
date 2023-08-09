package com.example.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.example.jokeapp.R
import com.example.jokeapp.domain.Communication


interface CommonUi {

    fun show(communication: Communication<State>)
    abstract class Abstract(
        private val text: String,
        private val punchline: String,
        @DrawableRes
        private val iconResId: Int
    ) : CommonUi {

        override fun show(communication: Communication<State>) = with(communication) {
            this.map(State.Initial("$text\n$punchline", iconResId))
        }
    }

    data class Base(private val text: String,private val  punchline: String) :
        Abstract(text, punchline, R.drawable.favorite_border_36)

    data class Favorite(private val text: String, private val punchline: String) :
        Abstract(text, punchline, R.drawable.favorite_36)

    data class Failed(private val text: String) : Abstract(text, "", 0) {
        override fun show(communication: Communication<State>) = with(communication) {
            this.map(State.Failed(text, 0))
        }
    }
}