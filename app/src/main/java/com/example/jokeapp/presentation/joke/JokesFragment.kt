package com.example.jokeapp.presentation.joke

import com.example.jokeapp.R
import com.example.jokeapp.presentation.BaseFragment

class JokesFragment : BaseFragment<JokesViewModel, Int>() {

    override fun getViewModelClass(): Class<JokesViewModel> = JokesViewModel::class.java

    override fun checkBoxText() = R.string.show_favorite_joke

    override fun actionButtonText() = R.string.get_joke
}