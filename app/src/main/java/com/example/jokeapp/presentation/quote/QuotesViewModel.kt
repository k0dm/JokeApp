package com.example.jokeapp.presentation.quote

import com.example.jokeapp.domain.CommonInteractor
import com.example.jokeapp.presentation.CommonViewModel
import com.example.jokeapp.presentation.StateCommunication


class QuotesViewModel(interactor: CommonInteractor<String>, communication: StateCommunication<String>) :
    CommonViewModel.Abstract<String>("QuotesVM", interactor, communication)