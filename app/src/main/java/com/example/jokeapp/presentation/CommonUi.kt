package com.example.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.example.jokeapp.R
import com.example.jokeapp.core.ShowImageView
import com.example.jokeapp.core.ShowText


interface CommonUi<E> {

    fun change(listener: FavoriteItemClickListener<E>)
    fun show(communication: Communication<State, E>)
    fun show(showText: ShowText)
    fun show(showImageView: ShowImageView)
    fun matches(id: E): Boolean

    fun same(commonUi: CommonUi<E>): Boolean

    abstract class Abstract<E>(
        private val firstText: String,
        private val secondText: String,
        @DrawableRes
        private val iconResId: Int
    ) : CommonUi<E> {

        override fun show(communication: Communication<State, E>) {
            communication.map(State.Initial("$firstText\n$secondText", iconResId))
        }

        override fun show(showText: ShowText) {
            showText.show("$firstText\n$secondText")
        }

        override fun show(showImageView: ShowImageView) {
            showImageView.show(iconResId)
        }

        //TODO fix dry
        override fun change(listener: FavoriteItemClickListener<E>) = Unit

        override fun matches(id: E): Boolean = false

        override fun same(commonUi: CommonUi<E>) = false
    }

    data class Base<E>(
        private val id: E,
        private val text: String,
        private val punchline: String
    ) : Abstract<E>(text, punchline, R.drawable.favorite_border_36) {
        override fun change(listener: FavoriteItemClickListener<E>) = listener.change(id)
        override fun matches(id: E) = this.id == id
        override fun same(commonUi: CommonUi<E>) = commonUi == this
    }

    data class Favorite<E>(
        private val id: E,
        private val text: String,
        private val punchline: String
    ) : Abstract<E>(text, punchline, R.drawable.favorite_36) {
        override fun change(listener: FavoriteItemClickListener<E>) = listener.change(id)
        override fun matches(id: E) = this.id == id
        override fun same(commonUi: CommonUi<E>) = commonUi == this
    }

    data class Failed<E>(private val text: String) : Abstract<E>(text, "", 0) {
        override fun show(communication: Communication<State, E>) = with(communication) {
            this.map(State.Failed(text, 0))
        }
    }
}