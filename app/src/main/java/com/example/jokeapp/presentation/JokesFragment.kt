package com.example.jokeapp.presentation

import com.example.jokeapp.JokeApp
import com.example.jokeapp.R

class JokesFragment : BaseFragment<Int>() {

    override fun getViewModel(app: JokeApp) = app.jokeViewModel

    override fun getCommunication(app: JokeApp) = app.jokeCommunication

    override fun checkBoxText() = R.string.show_favorite_joke

    override fun actionButtonText()= R.string.get_joke
}