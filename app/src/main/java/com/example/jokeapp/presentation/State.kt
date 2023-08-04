package com.example.jokeapp.presentation

import androidx.annotation.DrawableRes
import com.example.jokeapp.views.ShowButton
import com.example.jokeapp.views.ShowImageView
import com.example.jokeapp.views.ShowProgressBar
import com.example.jokeapp.views.ShowText

interface State {

    fun show(
        progress: ShowProgressBar,
        button: ShowButton,
        textView: ShowText,
        imageButton: ShowImageView
    )

    fun show(progress: ShowProgressBar, button: ShowButton)
    fun show(textView: ShowText, imageButton: ShowImageView)

    abstract class Info(private val text: String, @DrawableRes private val iconResId: Int) :
        State {

        override fun show(
            progress: ShowProgressBar,
            button: ShowButton,
            textView: ShowText,
            imageButton: ShowImageView
        ) {
            show(progress, button)
            show(textView, imageButton)
        }

        override fun show(progress: ShowProgressBar, button: ShowButton) {
            progress.show(false)
            button.show(true)
        }

        override fun show(textView: ShowText, imageButton: ShowImageView) {
            textView.show(text)
            imageButton.show(iconResId)
        }
    }

    class Progress : State {
        override fun show(
            progress: ShowProgressBar,
            button: ShowButton,
            textView: ShowText,
            imageButton: ShowImageView
        ) {
            show(progress, button)
        }

        override fun show(progress: ShowProgressBar, button: ShowButton) {
            progress.show(true)
            button.show(false)
        }

        override fun show(textView: ShowText, imageButton: ShowImageView) {
            textView.show("")
            imageButton.show(0)
        }

    }

    class Initial(text: String, @DrawableRes iconResId: Int) :
        Info(text, iconResId)

    class Failed(text: String, @DrawableRes iconResId: Int) :
        Info(text, iconResId)
}

