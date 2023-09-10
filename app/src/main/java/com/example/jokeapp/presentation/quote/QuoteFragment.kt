package com.example.jokeapp.presentation.quote

import com.example.jokeapp.R
import com.example.jokeapp.presentation.BaseFragment

class QuoteFragment : BaseFragment<QuotesViewModel, String>() {

    override fun getViewModelClass(): Class<QuotesViewModel> = QuotesViewModel::class.java

    override fun checkBoxText() = R.string.show_favorite_quote

    override fun actionButtonText() = R.string.get_quote
}